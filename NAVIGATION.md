# Project Navigation Guide

Quick links to all project documentation and resources.

## 📖 Documentation

### Getting Started
- **[README.md](README.md)** - Main project documentation
  - Project overview and features
  - Installation instructions
  - Usage guide
  - API documentation
  - Technology stack
  - Troubleshooting

- **[QUICKSTART.md](QUICKSTART.md)** - 5-minute setup guide
  - Prerequisites
  - Quick setup steps
  - Common issues
  - Testing commands

### Technical Documentation
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System architecture
  - Architecture diagrams
  - Component details
  - Data flow
  - Communication protocol
  - Scalability considerations

- **[DEVELOPMENT.md](DEVELOPMENT.md)** - Developer guide
  - Development environment setup
  - Code structure
  - Adding new features
  - Testing procedures
  - Contribution guidelines

### Project Information
- **[IMPLEMENTATION.md](IMPLEMENTATION.md)** - Implementation summary
  - What was built
  - Code statistics
  - Test results
  - Key achievements
  - Future enhancements

- **[FEATURES.md](FEATURES.md)** - Complete feature list
  - Backend features
  - Frontend features
  - Data features
  - Documentation features
  - 150+ implemented features

- **[SCREENSHOTS.md](SCREENSHOTS.md)** - UI documentation
  - Dashboard overview
  - Tab descriptions
  - Usage flow
  - Design features

## 💻 Source Code

### Python Backend
```
python_backend/
├── app.py              # Flask REST API server
├── data_analyzer.py    # Statistical analysis engine
└── requirements.txt    # Python dependencies
```

**Key Files:**
- [app.py](python_backend/app.py) - 8 REST API endpoints
- [data_analyzer.py](python_backend/data_analyzer.py) - Statistical analysis functions
- [requirements.txt](python_backend/requirements.txt) - Flask, Pandas, NumPy, etc.

### Java Frontend
```
java_frontend/
├── pom.xml            # Maven configuration
└── src/main/java/com/census/dashboard/
    ├── CensusDashboardApp.java  # Main JavaFX application
    └── ApiClient.java           # REST API client
```

**Key Files:**
- [pom.xml](java_frontend/pom.xml) - Maven dependencies
- [CensusDashboardApp.java](java_frontend/src/main/java/com/census/dashboard/CensusDashboardApp.java) - Main UI
- [ApiClient.java](java_frontend/src/main/java/com/census/dashboard/ApiClient.java) - API integration

### Sample Data
```
data/
└── census_data.csv    # 94 census records
```

**Data File:**
- [census_data.csv](data/census_data.csv) - Sample census dataset

## 🛠️ Tools & Scripts

- **[test_api.sh](test_api.sh)** - Automated API testing script
- **[.gitignore](.gitignore)** - Git ignore rules

## 🚀 Quick Actions

### Start the Application
```bash
# Terminal 1: Backend
cd python_backend
pip install -r requirements.txt
python app.py

# Terminal 2: Frontend
cd java_frontend
mvn javafx:run
```

### Run Tests
```bash
./test_api.sh
```

### Build Java Project
```bash
cd java_frontend
mvn clean package
```

## 📊 Project Statistics

- **Total Files**: 17
- **Python Code**: 416 lines
- **Java Code**: 584 lines
- **Documentation**: 1,340+ lines
- **API Endpoints**: 8
- **Features**: 150+

## 🔗 Quick Reference

| Topic | Document |
|-------|----------|
| Installation | [README.md](README.md#installation--setup) |
| API Endpoints | [README.md](README.md#api-endpoints) |
| System Design | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Adding Features | [DEVELOPMENT.md](DEVELOPMENT.md#adding-new-features) |
| Testing | [DEVELOPMENT.md](DEVELOPMENT.md#testing) |
| Troubleshooting | [README.md](README.md#troubleshooting) |
| Contributing | [DEVELOPMENT.md](DEVELOPMENT.md#contributing) |

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Sparsh Agarwal** (Devil179)

---

**Need help?** Start with [QUICKSTART.md](QUICKSTART.md) for a quick 5-minute setup, or [README.md](README.md) for comprehensive documentation.
