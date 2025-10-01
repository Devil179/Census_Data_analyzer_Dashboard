# Census Data Analyzer Dashboard

A comprehensive **Demographic Data Analysis and Visualization System** that combines Python and Java to provide an interactive dashboard for analyzing census and demographic datasets.

## Overview

This project integrates:
- **Python Backend**: Statistical analysis using Pandas and NumPy with REST API endpoints (Flask)
- **Java Frontend**: User-friendly JavaFX dashboard with charts, filters, and tables
- **Data Analysis**: Insights on age, education, income, and work hours
- **Export Functionality**: Export filtered data in multiple formats (CSV, Excel, JSON)

## Features

### Python Backend (Flask API)
- Statistical analysis using Pandas and NumPy
- REST API endpoints for data access
- Analysis endpoints:
  - Age distribution and statistics
  - Education level analysis
  - Income distribution
  - Work hours patterns
- Data filtering capabilities
- Multiple export formats (CSV, Excel, JSON)

### Java Frontend (JavaFX)
- Interactive dashboard with multiple views
- Rich data visualizations:
  - Bar charts for age and income distribution
  - Pie charts for education levels
  - Statistical summaries for work hours
- Tab-based navigation for different analyses
- Quick view selector for easy navigation
- Data refresh and export functionality

## Project Structure

```
Census_Data_analyzer_Dashboard/
├── python_backend/
│   ├── app.py                 # Flask API server
│   ├── data_analyzer.py       # Statistical analysis module
│   └── requirements.txt       # Python dependencies
├── java_frontend/
│   ├── pom.xml               # Maven configuration
│   └── src/main/java/com/census/dashboard/
│       ├── CensusDashboardApp.java  # Main JavaFX application
│       └── ApiClient.java           # REST API client
├── data/
│   └── census_data.csv       # Sample census dataset
└── README.md
```

## Installation & Setup

### Prerequisites
- Python 3.8 or higher
- Java 11 or higher
- Maven 3.6 or higher
- pip (Python package installer)

### Python Backend Setup

1. Navigate to the Python backend directory:
```bash
cd python_backend
```

2. Install required Python packages:
```bash
pip install -r requirements.txt
```

3. Start the Flask API server:
```bash
python app.py
```

The API server will start on `http://localhost:5000`

### Java Frontend Setup

1. Navigate to the Java frontend directory:
```bash
cd java_frontend
```

2. Build the project using Maven:
```bash
mvn clean install
```

3. Run the JavaFX application:
```bash
mvn javafx:run
```

## Usage

### Starting the System

1. **Start Python Backend** (in terminal 1):
```bash
cd python_backend
python app.py
```

2. **Start Java Frontend** (in terminal 2):
```bash
cd java_frontend
mvn javafx:run
```

### Using the Dashboard

1. **Overview Tab**: View comprehensive statistics across all categories
2. **Age Analysis**: Explore age distribution with bar charts
3. **Education Analysis**: Visualize education levels with pie charts
4. **Income Analysis**: Analyze income distribution
5. **Work Hours Analysis**: Review work hours patterns and statistics

### Data Operations

- **Refresh Data**: Click "Refresh Data" button to reload statistics
- **Export Data**: Choose export format (CSV, Excel, or JSON) and save data
- **Quick View**: Use dropdown selector for quick navigation between views

## API Endpoints

### Base URL: `http://localhost:5000/api`

- `GET /` - API information and available endpoints
- `POST /load` - Load census data from file
- `GET /statistics` - Get all statistics
- `GET /statistics/age` - Get age statistics
- `GET /statistics/education` - Get education statistics
- `GET /statistics/income` - Get income statistics
- `GET /statistics/work-hours` - Get work hours statistics
- `GET /data` - Get raw data with optional filters
- `POST /export` - Export data to file

## Data Format

The census dataset includes the following fields:
- `age`: Age of the individual
- `workclass`: Type of employment
- `education`: Education level
- `marital-status`: Marital status
- `occupation`: Occupation category
- `race`: Race
- `sex`: Gender
- `hours-per-week`: Average work hours per week
- `income`: Income level (<=50K or >50K)

## Technologies Used

### Backend
- **Python 3.8+**
- **Flask 3.0**: Web framework for REST API
- **Pandas 2.1**: Data manipulation and analysis
- **NumPy 1.26**: Numerical computing
- **Flask-CORS**: Cross-origin resource sharing

### Frontend
- **Java 11+**
- **JavaFX 17**: GUI framework
- **Maven**: Build and dependency management
- **Gson**: JSON parsing
- **OkHttp**: HTTP client for API communication

## Development

### Adding New Analysis Features

1. **Backend**: Add new analysis methods in `data_analyzer.py`
2. **API**: Create new endpoints in `app.py`
3. **Frontend**: Add new tabs or views in `CensusDashboardApp.java`

### Customizing Visualizations

- Modify chart types and styles in the JavaFX tab creation methods
- Add new chart types (Line, Area, Scatter) as needed
- Customize colors and themes in the JavaFX CSS

## Troubleshooting

### Python Backend Issues
- Ensure all dependencies are installed: `pip install -r requirements.txt`
- Check if port 5000 is available
- Verify data file path in `app.py`

### Java Frontend Issues
- Ensure Java 11+ is installed: `java -version`
- Install JavaFX if needed: `mvn javafx:run`
- Check API connection (backend must be running first)

### Connection Issues
- Verify backend is running on port 5000
- Check CORS settings in Flask app
- Ensure no firewall blocking localhost communication

## License

MIT License - see LICENSE file for details

## Author

Sparsh Agarwal

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
