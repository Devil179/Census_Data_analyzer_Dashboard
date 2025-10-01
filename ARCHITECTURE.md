# System Architecture

## Overview

The Census Data Analyzer Dashboard follows a client-server architecture with clear separation of concerns between data processing (Python) and visualization (Java).

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     USER INTERFACE LAYER                      │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         JavaFX Dashboard Application                  │   │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐     │   │
│  │  │  Overview  │  │    Age     │  │ Education  │     │   │
│  │  │    Tab     │  │  Analysis  │  │  Analysis  │ ... │   │
│  │  └────────────┘  └────────────┘  └────────────┘     │   │
│  │                                                       │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │    Charts: Bar, Pie, Statistics Display      │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ HTTP/REST
                            │ (JSON)
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     API LAYER (REST)                          │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Flask API Server                         │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │         API Endpoints                        │     │   │
│  │  │  - GET  /api/statistics                     │     │   │
│  │  │  - GET  /api/statistics/age                 │     │   │
│  │  │  - GET  /api/statistics/education           │     │   │
│  │  │  - GET  /api/statistics/income              │     │   │
│  │  │  - GET  /api/statistics/work-hours          │     │   │
│  │  │  - GET  /api/data                           │     │   │
│  │  │  - POST /api/export                         │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ Function Calls
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   DATA PROCESSING LAYER                       │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         CensusDataAnalyzer Module                     │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │    Statistical Analysis Functions            │     │   │
│  │  │  - get_age_statistics()                     │     │   │
│  │  │  - get_education_statistics()               │     │   │
│  │  │  - get_income_statistics()                  │     │   │
│  │  │  - get_work_hours_statistics()              │     │   │
│  │  │  - get_filtered_data()                      │     │   │
│  │  │  - export_data()                            │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  │                                                       │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │    Libraries: Pandas, NumPy                 │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ File I/O
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA STORAGE LAYER                       │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │            census_data.csv                            │   │
│  │  age, workclass, education, marital-status, ...      │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### 1. User Interface Layer (JavaFX)

**Technology**: Java 11, JavaFX 17

**Components**:
- `CensusDashboardApp.java`: Main application class
- `ApiClient.java`: REST API communication client

**Responsibilities**:
- Display data in charts and tables
- Handle user interactions (button clicks, tab selection)
- Format and present statistics
- Manage UI state and navigation

**Libraries**:
- JavaFX Controls: UI components
- JavaFX Charts: Bar charts, pie charts
- Gson: JSON parsing
- OkHttp: HTTP client

### 2. API Layer (Flask)

**Technology**: Python 3.8+, Flask 3.0

**Components**:
- `app.py`: Flask application with route handlers

**Responsibilities**:
- Expose REST endpoints for data access
- Handle HTTP requests/responses
- Route requests to data processing layer
- CORS handling for cross-origin requests
- Error handling and status codes

**Endpoints**:
- Statistics endpoints (GET): Return calculated statistics
- Data endpoint (GET/POST): Return raw or filtered data
- Export endpoint (POST): Generate and return file downloads

### 3. Data Processing Layer (Python)

**Technology**: Python 3.8+, Pandas 2.1, NumPy 1.26

**Components**:
- `data_analyzer.py`: CensusDataAnalyzer class

**Responsibilities**:
- Load census data from CSV files
- Perform statistical analysis:
  - Mean, median, standard deviation
  - Distribution calculations
  - Grouping and aggregation
- Filter data based on criteria
- Export data in multiple formats
- Data transformation and cleaning

**Key Methods**:
- `load_data()`: Load CSV into Pandas DataFrame
- `get_age_statistics()`: Age analysis with grouping
- `get_education_statistics()`: Education distribution
- `get_income_statistics()`: Income analysis
- `get_work_hours_statistics()`: Work hours patterns
- `get_filtered_data()`: Apply filters to dataset
- `export_data()`: Save to CSV, Excel, or JSON

### 4. Data Storage Layer

**Technology**: CSV files

**Components**:
- `census_data.csv`: Sample census dataset

**Structure**:
- Columns: age, workclass, education, marital-status, occupation, race, sex, hours-per-week, income
- Format: Comma-separated values
- Sample size: 100+ records

## Data Flow

### 1. Statistics Request Flow

```
User clicks "Load Age Data"
    ↓
JavaFX calls ApiClient.getAgeStatistics()
    ↓
HTTP GET request to /api/statistics/age
    ↓
Flask routes to get_age_statistics()
    ↓
analyzer.get_age_statistics() processes data
    ↓
Pandas calculates mean, median, groups
    ↓
JSON response returned to Flask
    ↓
ApiClient receives and parses JSON
    ↓
JavaFX updates chart with data
    ↓
User sees visualization
```

### 2. Export Data Flow

```
User clicks "Export Data"
    ↓
Dialog shows format options (CSV/Excel/JSON)
    ↓
User selects format
    ↓
ApiClient.exportData() called
    ↓
HTTP POST to /api/export with format
    ↓
Flask routes to export_data()
    ↓
analyzer.export_data() generates file
    ↓
File returned as HTTP attachment
    ↓
Browser/JavaFX saves file
    ↓
User receives exported file
```

## Communication Protocol

**Format**: JSON over HTTP/REST

**Request Example**:
```http
GET /api/statistics/age HTTP/1.1
Host: localhost:5000
Accept: application/json
```

**Response Example**:
```json
{
  "success": true,
  "data": {
    "mean": 38.64,
    "median": 39.0,
    "std": 12.25,
    "min": 18,
    "max": 79,
    "age_groups": {
      "0-18": 0,
      "19-30": 28,
      "31-45": 43,
      "46-60": 26,
      "60+": 3
    }
  }
}
```

## Scalability Considerations

1. **Backend**: Flask can be replaced with production WSGI server (Gunicorn, uWSGI)
2. **Data Storage**: Can be upgraded to database (PostgreSQL, MongoDB)
3. **Caching**: Add Redis for frequently accessed statistics
4. **Frontend**: Can be deployed as web application using JavaFX in browser
5. **API**: Can add authentication and rate limiting
6. **Analysis**: Can add more complex ML models and predictions

## Security Considerations

1. **CORS**: Configured to allow frontend access
2. **Input Validation**: API validates all inputs
3. **Error Handling**: Prevents information leakage
4. **File Uploads**: Validates file types and sizes
5. **Authentication**: Can be added for production use

## Performance Optimization

1. **Data Caching**: Statistics cached after first calculation
2. **Lazy Loading**: Data loaded only when requested
3. **Pagination**: Large datasets returned in pages
4. **Async Operations**: JavaFX operations don't block UI
5. **Connection Pooling**: HTTP client reuses connections
