from flask import Flask, jsonify, request, send_file
from flask_cors import CORS
from data_analyzer import CensusDataAnalyzer
import os
import json
from datetime import datetime

app = Flask(__name__)
CORS(app)  # Enable CORS for Java frontend

# Initialize analyzer with default data path
DATA_PATH = os.path.join(os.path.dirname(__file__), '..', 'data', 'census_data.csv')
analyzer = CensusDataAnalyzer()

@app.route('/')
def home():
    """API home endpoint."""
    return jsonify({
        'message': 'Census Data Analysis API',
        'version': '1.0',
        'endpoints': {
            '/api/load': 'Load census data',
            '/api/statistics': 'Get all statistics',
            '/api/statistics/age': 'Get age statistics',
            '/api/statistics/education': 'Get education statistics',
            '/api/statistics/income': 'Get income statistics',
            '/api/statistics/work-hours': 'Get work hours statistics',
            '/api/data': 'Get raw data with optional filters',
            '/api/export': 'Export data to file'
        }
    })

@app.route('/api/load', methods=['POST'])
def load_data():
    """Load census data from file."""
    try:
        data_path = request.json.get('path', DATA_PATH)
        if analyzer.load_data(data_path):
            return jsonify({
                'success': True,
                'message': 'Data loaded successfully',
                'records': len(analyzer.data) if analyzer.data is not None else 0
            })
        else:
            return jsonify({
                'success': False,
                'message': 'Failed to load data'
            }), 400
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/statistics', methods=['GET'])
def get_all_statistics():
    """Get comprehensive statistics."""
    try:
        if analyzer.data is None:
            # Try to load default data
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        stats = analyzer.get_all_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/statistics/age', methods=['GET'])
def get_age_statistics():
    """Get age statistics."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        stats = analyzer.get_age_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/statistics/education', methods=['GET'])
def get_education_statistics():
    """Get education statistics."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        stats = analyzer.get_education_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/statistics/income', methods=['GET'])
def get_income_statistics():
    """Get income statistics."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        stats = analyzer.get_income_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/statistics/work-hours', methods=['GET'])
def get_work_hours_statistics():
    """Get work hours statistics."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        stats = analyzer.get_work_hours_statistics()
        return jsonify({
            'success': True,
            'data': stats
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/data', methods=['GET', 'POST'])
def get_data():
    """Get raw data with optional filters."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        # Get filters from request
        filters = {}
        if request.method == 'POST' and request.json:
            filters = request.json.get('filters', {})
        
        # Apply filters if provided
        if filters:
            data = analyzer.get_filtered_data(filters)
        else:
            data = analyzer.data
        
        # Limit results for performance
        limit = int(request.args.get('limit', 1000))
        data_records = data.head(limit).to_dict('records')
        
        return jsonify({
            'success': True,
            'count': len(data),
            'limit': limit,
            'data': data_records
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/export', methods=['POST'])
def export_data():
    """Export data to file."""
    try:
        if analyzer.data is None:
            analyzer.load_data(DATA_PATH)
        
        if analyzer.data is None:
            return jsonify({
                'success': False,
                'message': 'No data loaded'
            }), 400
        
        # Get export parameters
        export_format = request.json.get('format', 'csv')
        filters = request.json.get('filters', {})
        
        # Apply filters if provided
        if filters:
            data = analyzer.get_filtered_data(filters)
        else:
            data = analyzer.data
        
        # Generate filename
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        extension = 'xlsx' if export_format == 'excel' else export_format
        filename = f'census_export_{timestamp}.{extension}'
        filepath = os.path.join('/tmp', filename)
        
        # Export data
        if analyzer.export_data(filepath, data, export_format):
            return send_file(filepath, as_attachment=True, download_name=filename)
        else:
            return jsonify({
                'success': False,
                'message': 'Export failed'
            }), 500
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

if __name__ == '__main__':
    print("Starting Census Data Analysis API Server...")
    print(f"Data path: {DATA_PATH}")
    app.run(host='0.0.0.0', port=5000, debug=True)
