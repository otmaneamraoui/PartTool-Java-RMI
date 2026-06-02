package parttool.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import parttool.common.dto.Token;
import parttool.common.dto.ToolCreatedDTO;
import parttool.common.dto.ToolDTO;
import parttool.common.dto.ToolSummaryDTO;
import parttool.common.exceptions.ToolException;

/**
 * CONTRAT RMI du serveur d'outils (V2).
 *
 * Rappel RMI (cours) :
 * - Une interface distante doit extends Remote.
 * - Toute méthode distante doit déclarer throws RemoteException.
 * - Les objets échangés doivent être sérialisables (Serializable).
 *
 * Idée V2 :
 * - Le client appelle declareTool(...) pour déclarer un outil.
 * - Le serveur Tools vérifiera que le Token est valide
 *   (en appelant AuthService.validate(token) sur le serveur Auth).
 */
public interface ToolService extends Remote {

    /**
     * Déclare un outil dans le système.
     *
     * @param token Token de session obtenu via AuthService.login(...)
     * @param tool  Données de l'outil (DTO sérialisable)
     * @return      Résultat de création (toolId + qrId simulé)
     *
     * @throws ToolException erreur métier (token invalide, champs invalides, etc.)
     * @throws RemoteException erreur technique RMI (réseau, registry, sérialisation...)
     */
    ToolCreatedDTO declareTool(Token token, ToolDTO tool)
            throws RemoteException, ToolException;

    /**
     * Liste les outils déclarés par l'utilisateur connecté.
     * (Optionnel mais pratique pour démontrer V2.)
     */
    List<ToolSummaryDTO> getMyTools(Token token)
            throws RemoteException, ToolException;

    /**
     * Récupère les caractéristiques d'un outil à partir d'un identifiant QR (simulé).
     * (Prépare V3/V4 où on scannera un vrai QR code.)
     */
    ToolDTO getToolByQrId(Token token, String qrId)
            throws RemoteException, ToolException;
}
