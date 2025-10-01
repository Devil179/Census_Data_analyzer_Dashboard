# Census Data Analyzer Dashboard - Screenshots

## Dashboard Overview

The Census Data Analyzer Dashboard is a JavaFX application that provides an interactive interface for analyzing census data.

### Main Window

The main window includes:
- **Top Section**: Title bar with control buttons
  - Refresh Data: Reload all statistics from API
  - Export Data: Export data in CSV, Excel, or JSON format
  - Quick View: Dropdown for fast navigation between tabs

- **Center Section**: Tab-based navigation with 5 main tabs:
  1. **Overview Tab**: Displays comprehensive statistics summary
  2. **Age Analysis Tab**: Bar chart showing age group distribution
  3. **Education Analysis Tab**: Pie chart showing education level distribution
  4. **Income Analysis Tab**: Bar chart showing income distribution
  5. **Work Hours Analysis Tab**: Statistics and chart for work hours patterns

- **Bottom Section**: Status area showing operation messages and results

### Tab Details

#### 1. Overview Tab
- Shows text-based comprehensive statistics
- Includes mean, median, min, max for numerical fields
- Distribution counts for categorical fields
- "Load Statistics" button to fetch all data from API

#### 2. Age Analysis Tab
- Interactive bar chart showing age group distribution
- Age groups: 0-18, 19-30, 31-45, 46-60, 60+
- X-axis: Age groups
- Y-axis: Count of individuals
- "Load Age Data" button to refresh chart

#### 3. Education Analysis Tab
- Interactive pie chart showing education level distribution
- Shows percentage for each education level
- Includes all education categories from dataset
- "Load Education Data" button to refresh chart

#### 4. Income Analysis Tab
- Bar chart showing income distribution
- Categories: <=50K, >50K
- "Load Income Data" button to refresh chart

#### 5. Work Hours Analysis Tab
- Text area showing statistical summary (mean, median, min, max)
- Bar chart showing work hours categories:
  - Part-time (<20 hours)
  - Normal (20-40 hours)
  - Full-time (40-60 hours)
  - Overtime (60+ hours)
- "Load Work Hours Data" button to refresh chart

## Usage Flow

1. Start Python backend (Flask API) on port 5000
2. Launch Java frontend (JavaFX application)
3. Navigate to desired tab (e.g., Age Analysis)
4. Click "Load" button to fetch data from API
5. View visualizations and statistics
6. Use "Export Data" to save results
7. Switch between tabs using tab bar or Quick View dropdown

## API Integration

The dashboard communicates with the Python backend via REST API:
- All data fetching is asynchronous
- Status messages appear in bottom status area
- Error handling with user-friendly messages
- Automatic data conversion from JSON to chart format

## Export Functionality

Export dialog allows choosing format:
- CSV: Comma-separated values
- Excel: .xlsx format using openpyxl
- JSON: JavaScript Object Notation

Exported files are downloaded through the browser or saved to specified location.

## Design Features

- Clean, modern UI with intuitive navigation
- Responsive layout that adapts to window size
- Color-coded charts for easy reading
- Status messages for all operations
- Error handling with descriptive messages
- Tab-based organization for clarity

Note: Since this is a headless environment, actual screenshots cannot be generated. When running locally, the application will display all these features in a native window.
