# 🔧 PART'TOOL — Distributed Tool-Sharing Platform (Java RMI)

A fully distributed application built with **Java RMI** (Remote Method Invocation) implementing a peer-to-peer tool lending platform called **PART'TOOL**. Users can register, declare tools, access storage locations, and borrow/return tools using a simulated QR code system.

---

## 🏗️ Architecture Overview

The system is composed of **5 independent Eclipse projects** communicating via Java RMI:

```
PartTool_common        ← Shared interfaces, DTOs, exceptions (no main)
PartTool_AuthServer    ← Authentication server (RMI registry port 1099)
PartTool_ToolsServer   ← Tool management server (RMI registry port 1100)
PartTool_StorageServer ← Storage location server (RMI registry port 1200)
PartTool_ClientUser    ← End-user console client
PartTool_ClientStorage ← In-storage terminal client (scans QR codes)
```

### RMI Communication Flow

```
ClientUser ──────────────────► AuthServer   (register, login → Token)
                                    │
ClientUser ──────────────────► ToolsServer  (declare tool, list, borrow, return)
                                    │ validates Token
                                    └──────────────► AuthServer

ClientStorage ───────────────► StorageServer (open storage with card+PIN)
                                    │ authenticates user
                                    └──────────────► AuthServer
```

---

## 📦 Module Breakdown

### `PartTool_common` — Shared Contracts
| Package | Contents |
|---------|----------|
| `rmi/` | `AuthService`, `ToolService`, `StorageService` — RMI remote interfaces |
| `dto/` | `Token`, `UserDTO`, `ToolDTO`, `ToolSummaryDTO`, `ToolCreatedDTO`, `StorageLocationDTO`, `ToolCategory`, `ToolStatus` |
| `exceptions/` | `AuthException`, `ToolException`, `StorageException` |
| `config/` | `RmiConstants` — ports and service names |

### `PartTool_AuthServer` — Authentication Service (port 1099)
| Class | Role |
|-------|------|
| `AuthServerMain` | Starts RMI registry, publishes `AuthService` |
| `AuthServiceImpl` | `register()`, `login()`, `validate()` |
| `User` / `UserStatus` | User model + status enum |
| `InMemoryUserRepository` | In-memory user store |
| `PinHasher` | PIN hashing utility |
| `TokenManager` | Session token generation & validation |

### `PartTool_ToolsServer` — Tool Management Service (port 1100)
| Class | Role |
|-------|------|
| `ToolsServerMain` | Starts registry, connects to AuthServer, publishes `ToolService` |
| `ToolServiceImpl` | `declareTool()`, `listTools()`, `listByCategory()`, `borrowTool()`, `returnTool()`, `getStats()` |
| `Tool` | Tool model (name, usage, description, weight, dimensions, QR ID, status) |
| `InMemoryToolRepository` | In-memory tool store |
| `QrIdGenerator` | Simulates QR code ID generation |
| `AuthServiceConnector` | Connects to AuthServer via RMI to validate tokens |

### `PartTool_StorageServer` — Storage Location Service (port 1200)
| Class | Role |
|-------|------|
| `StorageServerMain` | Starts registry, connects to AuthServer, publishes `StorageService` |
| `StorageServiceImpl` | `openStorage()`, `checkAccess()`, `listStorages()`, `getStats()` |
| `StorageLocation` | Location model (address, capacity, tools list) |
| `InMemoryStorageRepository` | In-memory storage store |
| `AuthServiceConnector` | Validates card+PIN against AuthServer |

### `PartTool_ClientUser` — User Console Client
| Class | Role |
|-------|------|
| `ClientMain` | Entry point |
| `ConsoleMenu` | Interactive menu: register, login, declare tool, browse, borrow, return |
| `AuthServiceConnector` | RMI lookup for AuthServer |
| `ToolServiceConnector` | RMI lookup for ToolServer |
| `ConsoleIO` | Input/output utilities |

### `PartTool_ClientStorage` — Storage Terminal Client
Simulates the PC terminal located inside a storage room. The user provides their card ID and PIN to open the storage and scan QR codes to borrow/return tools.

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 17+
- Eclipse IDE (recommended) or any Java IDE with multiple project support

### Startup Order

> ⚠️ **Always start in this order** — each server depends on the previous one.

```
1. AuthServer   → PartTool_AuthServer    → AuthServerMain.java
2. ToolsServer  → PartTool_ToolsServer   → ToolsServerMain.java
3. StorageServer→ PartTool_StorageServer → StorageServerMain.java
4. ClientUser   → PartTool_ClientUser    → ClientMain.java
5. (Optional)   → PartTool_ClientStorage → StorageClientMain.java
```

### Eclipse Setup
1. Import all 5 projects as existing Java projects
2. Add `PartTool_common` to the build path of all other projects
3. Run each `Main` class in the order listed above

---

## 🔑 Key Features

- **User registration** — generates a numeric access card ID
- **Secure login** — PIN hashing + session token management
- **Tool declaration** — name, category, description, weight, dimensions; generates a QR ID
- **Tool browsing** — list all tools or filter by category, view status (available/borrowed) and storage location
- **Borrow/Return** — scan QR code to borrow or return a tool
- **Storage access** — card + PIN authentication to unlock a storage room
- **Statistics** — operator dashboard: total tools, borrowed tools, tools per storage location

---

## 👨‍💻 Author

Project developed by:
- **AMRAOUI Otmane** — 1A/STRI

Academic project — University Paul Sabatier, Toulouse  
Course: Middleware & Distributed Computing (MCPR)

---

## 📜 License

Academic project — Not intended for commercial use.
