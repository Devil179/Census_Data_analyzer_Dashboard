# Development Guide

This guide is for developers who want to contribute to or customize the Census Data Analyzer Dashboard.

## Development Environment Setup

### Prerequisites
- Python 3.8+ with pip
- Java 11+ (JDK)
- Maven 3.6+
- Git
- A code editor (VS Code, IntelliJ IDEA, PyCharm, etc.)

### Initial Setup

1. **Clone the repository**:
```bash
git clone https://github.com/Devil179/Census_Data_analyzer_Dashboard.git
cd Census_Data_analyzer_Dashboard
```

2. **Set up Python environment**:
```bash
cd python_backend
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

3. **Verify Java and Maven**:
```bash
java -version
mvn -version
```

4. **Build Java project**:
```bash
cd java_frontend
mvn clean install
```

## Project Structure

```
Census_Data_analyzer_Dashboard/
├── python_backend/              # Python REST API
│   ├── app.py                  # Flask routes and endpoints
│   ├── data_analyzer.py        # Core analysis logic
│   └── requirements.txt        # Python dependencies
├── java_frontend/              # JavaFX Dashboard
│   ├── pom.xml                # Maven configuration
│   └── src/main/java/com/census/dashboard/
│       ├── CensusDashboardApp.java  # Main UI application
│       └── ApiClient.java           # REST API client
├── data/                       # Data files
│   └── census_data.csv        # Sample census dataset
└── docs/                      # Documentation
```

## Development Workflow

### Backend Development (Python)

#### Running in Development Mode
```bash
cd python_backend
export FLASK_ENV=development  # On Windows: set FLASK_ENV=development
python app.py
```

The server will auto-reload on code changes when in debug mode.

#### Adding a New Analysis Function

1. **Add method to `data_analyzer.py`**:
```python
def get_occupation_statistics(self) -> Dict[str, Any]:
    """Calculate occupation-related statistics."""
    if self.data is None or 'occupation' not in self.data.columns:
        return {}
    
    occupation_stats = {
        'distribution': self.data['occupation'].value_counts().to_dict(),
        'total_count': int(len(self.data))
    }
    return occupation_stats
```

2. **Add endpoint to `app.py`**:
```python
@app.route('/api/statistics/occupation', methods=['GET'])
def get_occupation_statistics():
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        stats = analyzer.get_occupation_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500
```

3. **Test the endpoint**:
```bash
curl http://localhost:5000/api/statistics/occupation | python -m json.tool
```

#### Testing Python Code

Run tests manually:
```bash
# Test data analyzer directly
python -c "from data_analyzer import CensusDataAnalyzer; \
           analyzer = CensusDataAnalyzer('../data/census_data.csv'); \
           print(analyzer.get_age_statistics())"

# Test API endpoints
./test_api.sh
```

### Frontend Development (Java)

#### Running in Development Mode
```bash
cd java_frontend
mvn javafx:run
```

For faster development, use:
```bash
mvn clean javafx:run
```

#### Adding a New Tab

1. **Create tab method in `CensusDashboardApp.java`**:
```java
private Tab createOccupationAnalysisTab() {
    Tab tab = new Tab("Occupation Analysis");
    tab.setClosable(false);

    VBox content = new VBox(15);
    content.setPadding(new Insets(15));

    Label titleLabel = new Label("Occupation Distribution Analysis");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    // Create chart
    PieChart pieChart = new PieChart();
    pieChart.setTitle("Occupation Distribution");

    Button loadBtn = new Button("Load Occupation Data");
    loadBtn.setOnAction(e -> {
        try {
            JsonObject response = apiClient.getOccupationStatistics();
            if (response.get("success").getAsBoolean()) {
                // Process and display data
                updateStatus("Occupation statistics loaded!");
            }
        } catch (Exception ex) {
            updateStatus("Error: " + ex.getMessage());
        }
    });

    content.getChildren().addAll(titleLabel, pieChart, loadBtn);
    tab.setContent(content);
    return tab;
}
```

2. **Add API method to `ApiClient.java`**:
```java
public JsonObject getOccupationStatistics() throws IOException {
    Request request = new Request.Builder()
            .url(BASE_URL + "/statistics/occupation")
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        String responseBody = response.body().string();
        return gson.fromJson(responseBody, JsonObject.class);
    }
}
```

3. **Add tab to main window**:
```java
tabPane.getTabs().addAll(
    createOverviewTab(),
    createAgeAnalysisTab(),
    createEducationAnalysisTab(),
    createIncomeAnalysisTab(),
    createWorkHoursAnalysisTab(),
    createOccupationAnalysisTab()  // Add new tab
);
```

#### Building for Production
```bash
mvn clean package
```

This creates an executable JAR in the `target/` directory.

## Code Style Guidelines

### Python
- Follow PEP 8 style guide
- Use type hints where appropriate
- Document functions with docstrings
- Keep functions focused and small
- Use meaningful variable names

Example:
```python
def calculate_statistics(data: pd.DataFrame, column: str) -> Dict[str, float]:
    """
    Calculate basic statistics for a given column.
    
    Args:
        data: DataFrame containing the data
        column: Column name to analyze
        
    Returns:
        Dictionary with mean, median, std, min, max
    """
    return {
        'mean': float(data[column].mean()),
        'median': float(data[column].median()),
        'std': float(data[column].std()),
        'min': float(data[column].min()),
        'max': float(data[column].max())
    }
```

### Java
- Follow Oracle Java Code Conventions
- Use meaningful class and method names
- Keep methods under 50 lines when possible
- Add JavaDoc comments for public methods
- Use proper exception handling

Example:
```java
/**
 * Loads statistics from the API and updates the chart.
 * 
 * @param chartType The type of chart to update
 * @throws IOException if API call fails
 */
private void loadAndUpdateChart(String chartType) throws IOException {
    JsonObject response = apiClient.getStatistics(chartType);
    if (response.get("success").getAsBoolean()) {
        updateChart(response.getAsJsonObject("data"));
        updateStatus("Data loaded successfully");
    } else {
        updateStatus("Failed to load data");
    }
}
```

## Debugging

### Python Backend
- Use Flask debug mode (already enabled in `app.py`)
- Add print statements or use Python debugger:
```python
import pdb; pdb.set_trace()
```
- Check Flask logs for errors
- Use curl or Postman to test API endpoints

### Java Frontend
- Use IntelliJ IDEA or Eclipse debugger
- Add breakpoints in JavaFX event handlers
- Check console output for errors
- Use Scene Builder for FXML layout design (if using FXML)

### Common Issues

1. **CORS errors**: Make sure Flask-CORS is installed and configured
2. **Connection refused**: Ensure Python backend is running first
3. **JavaFX not found**: Verify JavaFX is in Maven dependencies
4. **Data not loading**: Check file paths and permissions

## Testing

### Manual Testing
```bash
# Start backend
cd python_backend && python app.py

# In another terminal, run tests
./test_api.sh

# Start frontend
cd java_frontend && mvn javafx:run
```

### Adding Unit Tests

For Python (using pytest):
```python
# test_analyzer.py
import pytest
from data_analyzer import CensusDataAnalyzer

def test_age_statistics():
    analyzer = CensusDataAnalyzer('test_data.csv')
    stats = analyzer.get_age_statistics()
    assert 'mean' in stats
    assert 'median' in stats
    assert stats['mean'] > 0
```

For Java (using JUnit):
```java
// ApiClientTest.java
import org.junit.Test;
import static org.junit.Assert.*;

public class ApiClientTest {
    @Test
    public void testGetAgeStatistics() throws IOException {
        ApiClient client = new ApiClient();
        JsonObject response = client.getAgeStatistics();
        assertTrue(response.get("success").getAsBoolean());
    }
}
```

## Deployment

### Development Deployment
Already covered in previous sections.

### Production Deployment

#### Python Backend
Use a production WSGI server like Gunicorn:
```bash
pip install gunicorn
gunicorn -w 4 -b 0.0.0.0:5000 app:app
```

#### Java Frontend
Package as executable JAR:
```bash
mvn clean package
java -jar target/census-dashboard-1.0-SNAPSHOT.jar
```

Or create native installers using jpackage.

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Test thoroughly
5. Commit: `git commit -m 'Add amazing feature'`
6. Push: `git push origin feature/amazing-feature`
7. Open a Pull Request

## Resources

### Python/Flask
- [Flask Documentation](https://flask.palletsprojects.com/)
- [Pandas Documentation](https://pandas.pydata.org/docs/)
- [NumPy Documentation](https://numpy.org/doc/)

### Java/JavaFX
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Gson Documentation](https://github.com/google/gson)

### Tools
- [Postman](https://www.postman.com/) - API testing
- [Scene Builder](https://gluonhq.com/products/scene-builder/) - JavaFX UI design
- [DB Browser for SQLite](https://sqlitebrowser.org/) - If adding database

## Getting Help

- Check existing documentation (README.md, ARCHITECTURE.md)
- Review code comments
- Search GitHub issues
- Contact project maintainers

Happy coding! 🚀
