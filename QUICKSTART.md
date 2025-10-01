# Quick Start Guide

This guide will help you get the Census Data Analyzer Dashboard up and running quickly.

## Quick Setup (5 minutes)

### Step 1: Install Prerequisites

Ensure you have:
- Python 3.8+ (`python --version`)
- Java 11+ (`java -version`)
- Maven 3.6+ (`mvn -version`)
- pip (`pip --version`)

### Step 2: Start the Backend

Open a terminal and run:

```bash
# Navigate to project directory
cd Census_Data_analyzer_Dashboard

# Install Python dependencies
cd python_backend
pip install -r requirements.txt

# Start the Flask API server
python app.py
```

You should see:
```
Starting Census Data Analysis API Server...
 * Running on http://0.0.0.0:5000
```

Keep this terminal open!

### Step 3: Start the Frontend

Open a **new terminal** and run:

```bash
# Navigate to project directory
cd Census_Data_analyzer_Dashboard

# Build and run the JavaFX dashboard
cd java_frontend
mvn javafx:run
```

The dashboard window will open automatically.

### Step 4: Use the Dashboard

1. Click on any tab (Overview, Age Analysis, Education Analysis, etc.)
2. Click the "Load" button in each tab to fetch and display data
3. Use the "Refresh Data" button to reload all statistics
4. Use the "Export Data" button to save data in your preferred format

## Testing the API Directly

You can test the Python API using curl or a browser:

```bash
# Get all statistics
curl http://localhost:5000/api/statistics

# Get age statistics
curl http://localhost:5000/api/statistics/age

# Get education statistics
curl http://localhost:5000/api/statistics/education
```

## Common Issues

### Port 5000 already in use
Change the port in `python_backend/app.py`:
```python
app.run(host='0.0.0.0', port=5001, debug=True)
```
And update the URL in `java_frontend/src/main/java/com/census/dashboard/ApiClient.java`:
```java
private static final String BASE_URL = "http://localhost:5001/api";
```

### Frontend can't connect to backend
Make sure the Python backend is running first. Check the console output for errors.

### Maven build fails
Try cleaning the build:
```bash
mvn clean install -U
```

## Sample Data

The project includes a sample census dataset in `data/census_data.csv` with 100 records. You can replace this with your own census data as long as it maintains the same column structure.

## Next Steps

- Explore the full README.md for detailed documentation
- Customize the data analysis in `python_backend/data_analyzer.py`
- Add new visualizations in the JavaFX application
- Try different export formats (CSV, Excel, JSON)

Happy analyzing! 🎉
