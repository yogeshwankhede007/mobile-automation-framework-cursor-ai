#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Setting up Appium 2.x environment...${NC}"

# Install Appium 2.x globally
echo -e "${GREEN}Installing Appium 2.x...${NC}"
npm install -g appium@latest

# Install required drivers
echo -e "${GREEN}Installing Appium drivers...${NC}"
appium driver install uiautomator2
appium driver install xcuitest

# Install required plugins
echo -e "${GREEN}Installing Appium plugins...${NC}"
appium plugin install gestures
appium plugin install images
appium plugin install ocr
appium plugin install relaxed-caps

# Verify installations
echo -e "${GREEN}Verifying installations...${NC}"
echo "Installed drivers:"
appium driver list --installed

echo "Installed plugins:"
appium plugin list --installed

echo -e "${GREEN}Appium 2.x setup completed successfully!${NC}" 