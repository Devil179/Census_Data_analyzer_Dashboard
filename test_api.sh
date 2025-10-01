#!/bin/bash

# Census Data Analyzer Dashboard - Testing Script
# This script tests the Python backend API

echo "======================================"
echo "Census Data Analyzer Dashboard - Test"
echo "======================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Flask server is running
echo -n "Checking if API server is running... "
if curl -s http://localhost:5000/api >/dev/null 2>&1; then
    echo -e "${GREEN}OK${NC}"
else
    echo -e "${RED}FAILED${NC}"
    echo "Please start the Flask server first:"
    echo "  cd python_backend && python app.py"
    exit 1
fi

echo ""
echo "Testing API Endpoints:"
echo "----------------------"

# Test 1: Root endpoint
echo -n "1. Testing root endpoint... "
if curl -s http://localhost:5000/api | grep -q "Census Data Analysis API"; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 2: All statistics
echo -n "2. Testing /api/statistics... "
if curl -s http://localhost:5000/api/statistics | grep -q '"success": true'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 3: Age statistics
echo -n "3. Testing /api/statistics/age... "
if curl -s http://localhost:5000/api/statistics/age | grep -q '"mean"'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 4: Education statistics
echo -n "4. Testing /api/statistics/education... "
if curl -s http://localhost:5000/api/statistics/education | grep -q '"distribution"'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 5: Income statistics
echo -n "5. Testing /api/statistics/income... "
if curl -s http://localhost:5000/api/statistics/income | grep -q '"distribution"'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 6: Work hours statistics
echo -n "6. Testing /api/statistics/work-hours... "
if curl -s http://localhost:5000/api/statistics/work-hours | grep -q '"mean"'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

# Test 7: Data endpoint
echo -n "7. Testing /api/data... "
if curl -s "http://localhost:5000/api/data?limit=10" | grep -q '"count"'; then
    echo -e "${GREEN}PASS${NC}"
else
    echo -e "${RED}FAIL${NC}"
fi

echo ""
echo "======================================"
echo "Sample API Response:"
echo "======================================"
echo ""
echo "Age Statistics:"
curl -s http://localhost:5000/api/statistics/age | python3 -m json.tool | head -20
echo ""
echo "... (truncated)"
echo ""
echo "======================================"
echo "All tests completed!"
echo "======================================"
