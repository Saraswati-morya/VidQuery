# VidQuery


![Spring Boot Badge](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)

---

## 💡 About

This is a **Spring Boot** application that provides a powerful set of tools for interacting with YouTube's API. It allows users to fetch video metadata, including thumbnails, titles, descriptions, and tags, as well as generate related tags for a given topic. The application features a user-friendly interface using spring thymleaf with  both light and dark themes.

---

## ⚙️ Features

* **Thumbnail Extractor**: Get the highest quality thumbnail for any YouTube video URL.
* **Video Details**: Retrieve a video's title, description, tags, and upload date.
* **Related Tag Generator**: Find relevant tags for any topic to optimize content for discoverability.
* **API-Powered**: Leverages the official **YouTube Data API v3** for reliable and accurate data.
* **Themable UI**: A clean user interface with toggleable light and dark modes.

---

## 🚀 Getting Started

### Prerequisites

To run this project, you need to have the following installed:

* **Java 17** or a later version.
* **Maven 3.6.3** or a later version.
* A **Google Cloud Project** with the **YouTube Data API v3** enabled and a valid API key.

### Configuration

1.  **Clone the repository**:
    ```bash
    git clone [https://github.com/](https://github.com/)[YourUsername]/[YourRepositoryName].git
    ```
2.  **Navigate to the project directory**:
    ```bash
    cd [YourRepositoryName]
    ```
3.  **Configure your API Key**:
    * Open the `src/main/resources/application.properties` file.
    * Add your YouTube API key:
        ```properties
        youtube.api.key=[Your_API_Key_Here]
        ```

### Running the Application

Use Maven to run the application from the command line:

```bash
mvn spring-boot:run
```

---
### 🧑‍💻 Contact
[Saraswati Morya] - [saraswatimorya5@gmail.com] - [(https://www.linkedin.com/in/saraswati-morya-0b7a5b249/)]

Project Link: [https://github.com/Saraswati-morya/VidQuery](https://github.com/[YourUsername]/[YourRepositoryName])

<p align="right">Made with ❤️ using Spring Boot</p>

