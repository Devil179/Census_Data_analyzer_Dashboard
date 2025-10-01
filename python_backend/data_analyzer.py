import pandas as pd
import numpy as np
from typing import Dict, List, Any


class CensusDataAnalyzer:
    """Handles statistical analysis of census data using Pandas and NumPy."""
    
    def __init__(self, data_path: str = None):
        """Initialize the analyzer with optional data path."""
        self.data = None
        if data_path:
            self.load_data(data_path)
    
    def load_data(self, data_path: str):
        """Load census data from CSV file."""
        try:
            self.data = pd.read_csv(data_path)
            return True
        except Exception as e:
            print(f"Error loading data: {e}")
            return False
    
    def get_age_statistics(self) -> Dict[str, Any]:
        """Calculate age-related statistics."""
        if self.data is None or 'age' not in self.data.columns:
            return {}
        
        age_stats = {
            'mean': float(self.data['age'].mean()),
            'median': float(self.data['age'].median()),
            'std': float(self.data['age'].std()),
            'min': int(self.data['age'].min()),
            'max': int(self.data['age'].max()),
            'age_distribution': self.data['age'].value_counts().sort_index().to_dict()
        }
        
        # Age groups
        age_bins = [0, 18, 30, 45, 60, 100]
        age_labels = ['0-18', '19-30', '31-45', '46-60', '60+']
        self.data['age_group'] = pd.cut(self.data['age'], bins=age_bins, labels=age_labels)
        age_stats['age_groups'] = self.data['age_group'].value_counts().to_dict()
        
        return age_stats
    
    def get_education_statistics(self) -> Dict[str, Any]:
        """Calculate education-related statistics."""
        if self.data is None or 'education' not in self.data.columns:
            return {}
        
        education_stats = {
            'distribution': self.data['education'].value_counts().to_dict(),
            'total_count': int(len(self.data))
        }
        
        # Education level percentages
        education_pct = (self.data['education'].value_counts() / len(self.data) * 100).to_dict()
        education_stats['percentages'] = {k: float(v) for k, v in education_pct.items()}
        
        return education_stats
    
    def get_income_statistics(self) -> Dict[str, Any]:
        """Calculate income-related statistics."""
        if self.data is None or 'income' not in self.data.columns:
            return {}
        
        income_stats = {
            'distribution': self.data['income'].value_counts().to_dict(),
            'total_count': int(len(self.data))
        }
        
        # Income by education if available
        if 'education' in self.data.columns:
            income_by_education = self.data.groupby('education')['income'].value_counts().unstack(fill_value=0).to_dict()
            income_stats['by_education'] = income_by_education
        
        # Income percentages
        income_pct = (self.data['income'].value_counts() / len(self.data) * 100).to_dict()
        income_stats['percentages'] = {k: float(v) for k, v in income_pct.items()}
        
        return income_stats
    
    def get_work_hours_statistics(self) -> Dict[str, Any]:
        """Calculate work hours statistics."""
        if self.data is None or 'hours-per-week' not in self.data.columns:
            return {}
        
        work_hours = self.data['hours-per-week']
        
        hours_stats = {
            'mean': float(work_hours.mean()),
            'median': float(work_hours.median()),
            'std': float(work_hours.std()),
            'min': float(work_hours.min()),
            'max': float(work_hours.max())
        }
        
        # Work hours by income if available
        if 'income' in self.data.columns:
            hours_by_income = self.data.groupby('income')['hours-per-week'].agg(['mean', 'median']).to_dict()
            hours_stats['by_income'] = hours_by_income
        
        # Work hours distribution
        hours_bins = [0, 20, 40, 60, 100]
        hours_labels = ['Part-time (<20)', 'Normal (20-40)', 'Full-time (40-60)', 'Overtime (60+)']
        self.data['hours_category'] = pd.cut(work_hours, bins=hours_bins, labels=hours_labels)
        hours_stats['categories'] = self.data['hours_category'].value_counts().to_dict()
        
        return hours_stats
    
    def get_all_statistics(self) -> Dict[str, Any]:
        """Get comprehensive statistics from all categories."""
        return {
            'age': self.get_age_statistics(),
            'education': self.get_education_statistics(),
            'income': self.get_income_statistics(),
            'work_hours': self.get_work_hours_statistics()
        }
    
    def get_filtered_data(self, filters: Dict[str, Any]) -> pd.DataFrame:
        """Filter data based on provided criteria."""
        if self.data is None:
            return pd.DataFrame()
        
        filtered_data = self.data.copy()
        
        for column, value in filters.items():
            if column in filtered_data.columns:
                if isinstance(value, list):
                    filtered_data = filtered_data[filtered_data[column].isin(value)]
                else:
                    filtered_data = filtered_data[filtered_data[column] == value]
        
        return filtered_data
    
    def export_data(self, output_path: str, data: pd.DataFrame = None, format: str = 'csv'):
        """Export data to file."""
        if data is None:
            data = self.data
        
        if data is None or data.empty:
            return False
        
        try:
            if format == 'csv':
                data.to_csv(output_path, index=False)
            elif format == 'excel':
                data.to_excel(output_path, index=False, engine='openpyxl')
            elif format == 'json':
                data.to_json(output_path, orient='records', indent=2)
            return True
        except Exception as e:
            print(f"Error exporting data: {e}")
            return False
