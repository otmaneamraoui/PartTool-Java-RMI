package parttool.tools.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import parttool.common.dto.Token;
import parttool.common.dto.ToolCreatedDTO;
import parttool.common.dto.ToolDTO;
import parttool.common.dto.ToolStatus;
import parttool.common.dto.ToolSummaryDTO;
import parttool.common.exceptions.ToolException;
import parttool.common.rmi.AuthService;
import parttool.common.rmi.ToolService;
import parttool.tools.server.model.Tool;
import parttool.tools.server.repo.ToolRepository;
import parttool.tools.server.util.QrIdGenerator;

/**
 * Implémentation serveur du service d'outils (V2).
 *
 * RMI (cours) :
 * - extends UnicastRemoteObject : rend l'objet exportable à distance.
 * - implements ToolService : respecte le contrat partagé.
 *
 * IMPORTANT (V2) :
 * - Avant d'accepter une action, on vérifie que le client est authentifié
 *   en appelant AuthService.validate(token) sur le serveur Auth.
 */
public class ToolServiceImpl extends UnicastRemoteObject implements ToolService {

    private static final long serialVersionUID = 1L;

    private final ToolRepository repo;
    private final QrIdGenerator idGen;

    // Stub vers AuthService (obtenu par lookup depuis ToolsServerMain)
    private final AuthService auth;

    /**
     * Constructeur :
     * - doit déclarer throws RemoteException (car UnicastRemoteObject peut en lancer).
     */
    public ToolServiceImpl(ToolRepository repo, QrIdGenerator idGen, AuthService auth) throws RemoteException {
        super();
        this.repo = repo;
        this.idGen = idGen;
        this.auth = auth;
    }

    @Override
    public ToolCreatedDTO declareTool(Token token, ToolDTO tool) throws RemoteException, ToolException {

        // 1) Sécurité V2 : vérifier que le token est valide
        ensureAuthenticated(token);

        // 2) Vérifications simples (métier) sur les champs de l'outil
        validateToolInput(tool);

        // 3) Génération identifiants
        long toolId = idGen.nextToolId();
        String qrId = idGen.nextQrId(toolId);

        // 4) Création du modèle interne serveur (Tool) et stockage
        int ownerCardId = token.getUserCardId();

        Tool stored = new Tool(
                toolId,
                qrId,
                ownerCardId,
                tool.getName(),
                tool.getDescription(),
                tool.getCategory(),
                tool.getWeightKg(),
                tool.getLengthCm(),
                tool.getWidthCm(),
                tool.getHeightCm(),
                ToolStatus.AVAILABLE
        );

        repo.save(stored);

        // 5) Retour au client : résultat de création (DTO sérialisable)
        return new ToolCreatedDTO(toolId, qrId, "Outil déclaré avec succès");
    }

    @Override
    public List<ToolSummaryDTO> getMyTools(Token token) throws RemoteException, ToolException {
        ensureAuthenticated(token);

        int owner = token.getUserCardId();
        List<Tool> tools = repo.findByOwner(owner);

        // On renvoie un DTO "léger" pour affichage en liste
        List<ToolSummaryDTO> result = new ArrayList<>();
        for (Tool t : tools) {
            result.add(new ToolSummaryDTO(t.getId(), t.getQrId(), t.getName(), t.getStatus()));
        }
        return result;
    }

    @Override
    public ToolDTO getToolByQrId(Token token, String qrId) throws RemoteException, ToolException {
        ensureAuthenticated(token);

        if (qrId == null || qrId.isBlank()) {
            throw new ToolException("qrId manquant");
        }

        Tool t = repo.findByQrId(qrId);
        if (t == null) {
            throw new ToolException("Outil introuvable pour qrId=" + qrId);
        }

        // Sécurité simple : en V2, on limite à ses propres outils
        // (tu peux retirer cette contrainte si le sujet veut accès global)
        if (t.getOwnerCardId() != token.getUserCardId()) {
            throw new ToolException("Accès refusé : outil ne vous appartient pas");
        }

        // Conversion modèle interne -> DTO transférable
        return new ToolDTO(
                t.getName(),
                t.getDescription(),
                t.getCategory(),
                t.getWeightKg(),
                t.getLengthCm(),
                t.getWidthCm(),
                t.getHeightCm()
        );
    }

    /**
     * Vérifie via AuthService que l'utilisateur est bien connecté.
     * Si le token est invalide : on refuse.
     */
    private void ensureAuthenticated(Token token) throws ToolException {
        try {
            if (token == null) {
                throw new ToolException("Token manquant (non authentifié)");
            }

            boolean ok = auth.validate(token);
            if (!ok) {
                throw new ToolException("Token invalide ou expiré (non authentifié)");
            }
        } catch (RemoteException re) {
            // Si AuthServer est down => erreur technique masquée en erreur métier explicite
            throw new ToolException("Impossible de vérifier le token (AuthServer indisponible)", re);
        }
    }

    /**
     * Vérifications métier basiques des champs outil.
     * V2 : on reste simple, mais on empêche les données absurdes.
     */
    private void validateToolInput(ToolDTO tool) throws ToolException {
        if (tool == null) throw new ToolException("Données outil manquantes");

        if (tool.getName() == null || tool.getName().isBlank()) {
            throw new ToolException("Nom outil obligatoire");
        }

        if (tool.getWeightKg() < 0) throw new ToolException("Poids invalide");
        if (tool.getLengthCm() < 0) throw new ToolException("Longueur invalide");
        if (tool.getWidthCm() < 0) throw new ToolException("Largeur invalide");
        if (tool.getHeightCm() < 0) throw new ToolException("Hauteur invalide");
    }
}
