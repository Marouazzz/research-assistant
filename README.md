# 🔬 Smart Research Assistant

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring%20AI-Latest-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Gemini](https://img.shields.io/badge/Gemini%20API-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**AI-powered research management system built with Spring Boot, Spring AI, and Google Gemini API**

[Features](#-key-features) • [Architecture](#-system-architecture) • [Getting Started](#-getting-started) • [Documentation](#-detailed-workflow) • [API](#-api-reference)

</div>

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Detailed Workflow](#-detailed-workflow)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Testing](#-testing)

---

## 🎯 Overview

The **Smart Research Assistant** is a modern web application that leverages Google's Gemini AI to help researchers and students organize, summarize, and manage their research notes efficiently. The application features a clean, intuitive interface with real-time AI-powered summarization capabilities.

### 🌟 Key Features

<table>
<tr>
<td width="50%">

#### 🤖 AI-Powered Features
- ✨ **Instant Summarization** - Real-time text analysis with Gemini
- 🧠 **Context-Aware Processing** - Intelligent content understanding
- 🔄 **Multi-Model Support** - Flexible AI model integration
- 📊 **Token Analytics** - Track API usage and optimization

</td>
<td width="50%">

#### 📝 Research Management
- 📚 **Note Organization** - Structured multi-part documents
- 🏷️ **Smart Tagging** - Categorize and filter research
- 🔍 **Advanced Search** - Find notes quickly
- 💾 **Auto-Save** - Never lose your work

</td>
</tr>

</table>

---

## 🏗️ System Architecture

### High-Level Architecture

```mermaid
graph TB
    subgraph Client["🌐 Client Layer"]
        Browser["Web Browser"]
        Mobile["Mobile Device"]
    end
    
    subgraph Frontend["💻 Frontend Layer"]
    end
    
    subgraph Backend["⚙️ Spring Boot Backend"]
        Controller["🎮 Controller Layer<br/>REST Endpoints"]
        Service["⚡ Service Layer<br/>Business Logic"]
        Repository["💾 Repository Layer<br/>Data Access"]
    end
    
    subgraph Integration["🔌 Integration Layer"]
        SpringAI["Spring AI Framework"]
        GeminiService["Gemini Service"]
    end
    
    subgraph External["☁️ External Services"]
        GeminiAPI["Google Gemini API"]
        Database["MongoDB/PostgreSQL"]
    end
    
    Browser --> Frontend
    Mobile --> Frontend
    Frontend --> Controller
    Controller --> Service
    Service --> GeminiService
    Service --> Repository
    GeminiService --> SpringAI
    SpringAI --> GeminiAPI
    Repository --> Database
    
    style Client fill:#e1f5ff
    style Frontend fill:#fff4e1
    style Backend fill:#e8f5e9
    style Integration fill:#f3e5f5
    style External fill:#fce4ec
```

### Component Architecture

```mermaid
graph LR
    subgraph "Spring Boot Application"
        A[ResearchController] --> B[GeminiService]
        A --> C[NoteService]
        B --> D[Spring AI Client]
        C --> E[NoteRepository]
        
        F[ResearchRequest] --> A
        A --> G[ResearchResponse]
        
        H[GeminiConfig] -.-> B
        I[SecurityConfig] -.-> A
    end
    
    D --> J[Gemini API]
    E --> K[(Database)]
    
    style A fill:#4CAF50
    style B fill:#2196F3
    style C fill:#FF9800
    style D fill:#9C27B0
    style E fill:#F44336
```

### Request Flow Architecture

```mermaid
sequenceDiagram
    participant U as 👤 User
    participant F as 🌐 Frontend
    participant C as 🎮 Controller
    participant S as ⚡ Service
    participant AI as 🤖 Spring AI
    participant G as ☁️ Gemini API
    participant DB as 💾 Database
    
    U->>F: Select text & click "Summarize"
    F->>C: POST /api/v1/research/summarize
    C->>C: Validate request
    C->>S: generateSummary(request)
    S->>S: Build prompt
    S->>AI: call(prompt)
    AI->>G: HTTP Request
    G->>G: Process with AI model
    G-->>AI: Return summary
    AI-->>S: ChatResponse
    S->>S: Format response
    S->>DB: Save to history (optional)
    S-->>C: SummaryResponse
    C-->>F: JSON Response
    F-->>U: Display summary
```

---

## 📊 Detailed Workflow

### Phase 1: Text Summarization Pipeline

```mermaid
graph TD
    A[👤 User selects text] --> B{Text valid?}
    B -->|No| C[❌ Show error]
    B -->|Yes| D[📤 Send to API]
    
    D --> E[🎮 Controller receives request]
    E --> F[✅ Validate input]
    F --> G[⚡ GeminiService.generateSummary]
    
    G --> H[🔨 Build AI prompt]
    H --> I[📝 Add context & instructions]
    I --> J[🤖 Call Spring AI]
    
    J --> K[☁️ Gemini API processing]
    K --> L[🧠 AI model generation]
    L --> M[📊 Token calculation]
    
    M --> N[✨ Format response]
    N --> O[📈 Add metadata]
    O --> P[💾 Cache result optional]
    
    P --> Q[📤 Return to frontend]
    Q --> R[🎨 Render summary]
    R --> S[💚 Show success message]
    
    style A fill:#e3f2fd
    style K fill:#f3e5f5
    style R fill:#e8f5e9
```

### Phase 2: Note Management Flow

```mermaid
stateDiagram-v2
    [*] --> Draft: User creates note
    Draft --> Validating: Click "Save"
    
    Validating --> ValidationError: Invalid data
    ValidationError --> Draft: Fix errors
    
    Validating --> Processing: Valid data
    Processing --> AIEnrichment: Request summary
    
    AIEnrichment --> Summarizing: Call Gemini
    Summarizing --> Enriched: Summary ready
    
    Enriched --> Saving: Persist to DB
    Saving --> Saved: Success
    
    Saved --> Published: User publishes
    Published --> [*]
    
    Saving --> SaveError: DB error
    SaveError --> Processing: Retry
```

### Phase 3: Query & Retrieval Flow

```mermaid
graph LR
    A[🔍 User Search] --> B{Query Type}
    
    B -->|By ID| C[Direct Fetch]
    B -->|By Filter| D[Filter Query]
    B -->|Full Text| E[Search Index]
    
    C --> F[(Database)]
    D --> F
    E --> G[(Search Engine)]
    
    F --> H[📋 Results]
    G --> H
    
    H --> I{Transform}
    I --> J[DTO Mapping]
    J --> K[Add Metadata]
    K --> L[Pagination]
    
    L --> M[📤 API Response]
    M --> N[🎨 Render UI]
    
    style A fill:#e1f5ff
    style H fill:#fff4e1
    style N fill:#e8f5e9
```




## 🛠️ Tech Stack

### Backend Technologies

```mermaid
mindmap
  root((Backend Stack))
    Core
      Java 17+
      Spring Boot 3.x
      Maven 3.8+
    Web Layer
      Spring Web MVC
      REST API
      JSON Processing
    AI Integration
      Spring AI
      Gemini API Client
      Prompt Engineering
    Data Layer
      Spring Data JPA
    Security
      Spring Security
    Testing
      JUnit 5

```

### Frontend Technologies

```mermaid
mindmap
  root((Frontend Stack))
    Structure
      HTML5
      Semantic Markup
      Accessibility
    Styling
      CSS3
      Flexbox/Grid
      Animations
    Interactivity
      Vanilla JavaScript
      Fetch API
      DOM Manipulation
    UI/UX
      Responsive Design

```


## 🚀 Getting Started

### Prerequisites Checklist

```mermaid
graph LR
    A[✅ Prerequisites] --> B[Java 17+]
    A --> C[Maven 3.8+]
    A --> D[IDE]
    A --> E[Git]
    A --> F[Gemini API Key]
    
    B --> B1[java -version]
    C --> C1[mvn -version]
    D --> D1[IntelliJ IDEA]
    D --> D2[VS Code]
    E --> E1[git --version]
    F --> F1[Google Cloud Console]
    
    style A fill:#4CAF50,color:#fff
```

### Installation Steps

```mermaid
graph TD
    A[Start] --> B[Clone Repository]
    B --> C[Configure API Keys]
    C --> D[Install Dependencies]
    D --> E[Build Project]
    E --> F[Run Application]
    F --> G[Access UI]
    G --> H[Test Features]
    H --> I[End]
    
    B -.-> B1["git clone <repo-url>"]
    C -.-> C1[Edit application.properties]
    D -.-> D1[mvn clean install]
    E -.-> E1[mvn package]
    F -.-> F1[mvn spring-boot:run]
    G -.-> G1[http://localhost:8080]
    
    style A fill:#4CAF50,color:#fff
    style I fill:#4CAF50,color:#fff
    style F fill:#2196F3,color:#fff
```

### Quick Start Commands

```bash
# 1️⃣ Clone the repository
git clone https://github.com/yourusername/smart-research-assistant.git
cd smart-research-assistant

# 2️⃣ Set up configuration
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties and add your Gemini API key

# 3️⃣ Install dependencies
mvn clean install

# 4️⃣ Run the application
mvn spring-boot:run

# 5️⃣ Access the application
# Open browser: http://localhost:8080
```

### Configuration Guide

Create `src/main/resources/application.properties`:

```properties
# ============================================
# Server Configuration
# ============================================
server.port=8080
server.servlet.context-path=/

# ============================================
# Spring Application
# ============================================
spring.application.name=smart-research-assistant

# ============================================
# Gemini AI Configuration
# ============================================
spring.ai.gemini.api-key=${GEMINI_API_KEY:your-api-key-here}
spring.ai.gemini.model=gemini-pro
spring.ai.gemini.temperature=0.7
spring.ai.gemini.max-tokens=2048

# ============================================
# Database Configuration (MongoDB)
# ============================================
spring.data.mongodb.uri=mongodb://localhost:27017/research_assistant
spring.data.mongodb.database=research_assistant

# ============================================
# Logging Configuration
# ============================================
logging.level.root=INFO
logging.level.com.yourpackage.research=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# ============================================
# Cache Configuration
# ============================================
spring.cache.type=simple
spring.cache.cache-names=summaries,notes
```

---

## 📁 Project Structure

```mermaid
graph TD
    A[smart-research-assistant/] --> B[src/]
    A --> C[pom.xml]
    A --> D[README.md]
    A --> E[.gitignore]
    
    B --> F[main/]
    B --> G[test/]
    
    F --> H[java/com/yourpackage/research/]
    F --> I[resources/]
    
    H --> J[ResearchAssistantApplication.java]
    H --> K[controller/]
    H --> L[service/]
    H --> M[model/]
    H --> N[repository/]
    H --> O[config/]
    H --> P[exception/]
    
    K --> K1[ResearchController.java]
    L --> L1[GeminiService.java]
    L --> L2[NoteService.java]
    M --> M1[Note.java]
    M --> M2[ResearchRequest.java]
    M --> M3[ResearchResponse.java]
    N --> N1[NoteRepository.java]
    O --> O1[GeminiConfig.java]
    O --> O2[SecurityConfig.java]
    P --> P1[GlobalExceptionHandler.java]
    
    I --> Q[application.properties]
    I --> R[static/]
    I --> S[templates/]
    
    R --> R1[css/styles.css]
    R --> R2[js/app.js]
    R --> R3[index.html]
    
    G --> T[java/com/yourpackage/research/]
    T --> U[ResearchAssistantTests.java]
    T --> V[GeminiServiceTests.java]
    
    style A fill:#4CAF50,color:#fff
    style H fill:#2196F3,color:#fff
    style I fill:#FF9800,color:#fff
```

### Key Components Description

```mermaid
graph LR
    subgraph Controllers
        A[ResearchController<br/>API Endpoints]
    end
    
    subgraph Services
        B[GeminiService<br/>AI Integration]
        C[NoteService<br/>Note Management]
    end
    
    subgraph Models
        D[Request DTOs]
        E[Response DTOs]
        F[Entities]
    end
    
    subgraph Repository
        G[NoteRepository<br/>Data Access]
    end
    
    subgraph Configuration
        H[GeminiConfig]
        I[SecurityConfig]
        J[CorsConfig]
    end
    
    A --> B
    A --> C
    B --> D
    B --> E
    C --> F
    C --> G
    H -.-> B
    I -.-> A
    
    style Controllers fill:#4CAF50,color:#fff
    style Services fill:#2196F3,color:#fff
    style Models fill:#FF9800,color:#fff
    style Repository fill:#9C27B0,color:#fff
    style Configuration fill:#F44336,color:#fff
```


### Endpoint Documentation

#### 1. Summarize Text

```mermaid
sequenceDiagram
    participant C as Client
    participant API as API Server
    participant AI as Gemini AI
    
    C->>API: POST /api/v1/research/summarize
    Note over C,API: {text, options}
    
    API->>API: Validate request
    API->>AI: Generate summary
    AI-->>API: Summary response
    API->>API: Format response
    API-->>C: 200 OK
    Note over API,C: {summary, metadata}
```

**Request:**
```json
{
  "text": "Your research content here...",
  "options": {
    "maxLength": 150,
    "style": "academic",
    "language": "en"
  }
}
```

**Response:**
```json
{
  "summary": "Generated summary...",
  "metadata": {
    "wordCount": 42,
    "processingTime": 1250,
    "model": "gemini-pro"
  }
}
```

#### 2. Create Note

```mermaid
sequenceDiagram
    participant C as Client
    participant API as API Server
    participant DB as Database
    
    C->>API: POST /api/v1/research/notes
    API->>API: Validate note data
    API->>DB: Save note
    DB-->>API: Saved note ID
    API-->>C: 201 Created
    Note over API,C: {noteId, url}
```

#### 3. API Status Codes

```mermaid
graph TD
    A[HTTP Status Codes] --> B[2xx Success]
    A --> C[4xx Client Error]
    A --> D[5xx Server Error]
    
    B --> B1[200 OK]
    B --> B2[201 Created]
    B --> B3[204 No Content]
    
    C --> C1[400 Bad Request]
    C --> C2[401 Unauthorized]
    C --> C3[404 Not Found]
    C --> C4[429 Too Many Requests]
    
    D --> D1[500 Internal Server Error]
    D --> D2[503 Service Unavailable]
    
    style B fill:#4CAF50,color:#fff
    style C fill:#FF9800,color:#fff
    style D fill:#F44336,color:#fff
```

---

## 🧪 Testing

### Test Architecture

```mermaid
graph TD
    A[Test Suite] --> B[Unit Tests]
    A --> C[Integration Tests]
    A --> D[E2E Tests]
    
    B --> B1[Controller Tests]
    B --> B2[Service Tests]
    B --> B3[Repository Tests]
    
    C --> C1[API Integration]
    C --> C2[Database Integration]
    C --> C3[External Services]
    
    D --> D1[UI Flow Tests]
    D --> D2[User Journey Tests]
    
    B1 -.-> E[JUnit 5]
    B2 -.-> E
    B3 -.-> F[Mockito]
    C1 -.-> G[TestContainers]
    D1 -.-> H[Selenium]
    
    style A fill:#4CAF50,color:#fff
    style B fill:#2196F3,color:#fff
    style C fill:#FF9800,color:#fff
    style D fill:#9C27B0,color:#fff
```

### Test Coverage Flow

```mermaid
graph LR
    A[Write Code] --> B[Write Tests]
    B --> C[Run Tests]
    C --> D{All Pass?}
    D -->|No| E[Fix Issues]
    E --> B
    D -->|Yes| F[Generate Coverage]
    F --> G{Coverage > 80%?}
    G -->|No| H[Add More Tests]
    H --> B
    G -->|Yes| I[Commit Code]
    
    style I fill:#4CAF50,color:#fff
    style E fill:#F44336,color:#fff
```

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=GeminiServiceTest

# Run integration tests only
mvn verify -P integration-tests

# Generate coverage report
mvn clean test jacoco:report
# View report: target/site/jacoco/index.html
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Support & Community

<div align="center">

### 💬 Get Help

[![GitHub Issues](https://img.shields.io/badge/Issues-Create%20Issue-red?style=for-the-badge&logo=github)](https://github.com/Marouazzz/research-assistant/issues)


### ⭐ Show Your Support

If you find this project helpful, please consider giving it a star!

[![Stars](https://img.shields.io/github/stars/Marouazzz/research-assistant?style=social)](https://github.com/Marouazzz/research-assistant)

</div>

---

<div align="center">

**Made with ❤️ by MAROUAZZZ**

*Powered by Spring Boot • Enhanced by AI • Built for Researchers*

</div>
