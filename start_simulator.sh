#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Checking iOS simulator status...${NC}"

# Check if simulator is running
SIMULATOR_RUNNING=$(xcrun simctl list devices | grep "iPhone 16 Pro Max" | grep "Booted")

if [ -z "$SIMULATOR_RUNNING" ]; then
    echo -e "${RED}iPhone 16 Pro Max simulator is not running. Starting it...${NC}"
    
    # Get the simulator UDID
    SIMULATOR_UDID=$(xcrun simctl list devices | grep "iPhone 16 Pro Max" | grep -E -o -i "([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})")
    
    if [ -z "$SIMULATOR_UDID" ]; then
        echo -e "${RED}Could not find iPhone 16 Pro Max simulator. Creating it...${NC}"
        
        # Create the simulator
        xcrun simctl create "iPhone 16 Pro Max" "iPhone 16 Pro Max"
        
        # Get the new simulator UDID
        SIMULATOR_UDID=$(xcrun simctl list devices | grep "iPhone 16 Pro Max" | grep -E -o -i "([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})")
    fi
    
    # Boot the simulator
    xcrun simctl boot $SIMULATOR_UDID
    
    # Open Simulator app
    open -a Simulator
    
    # Wait for simulator to be ready
    echo -e "${BLUE}Waiting for simulator to be ready...${NC}"
    sleep 30
    
    # Update config.properties with simulator UDID
    sed -i '' "s/ios.simulator.udid=.*/ios.simulator.udid=$SIMULATOR_UDID/" src/test/resources/config.properties
    
    echo -e "${GREEN}iPhone 16 Pro Max simulator is now running with UDID: $SIMULATOR_UDID${NC}"
else
    # Get the running simulator UDID
    SIMULATOR_UDID=$(xcrun simctl list devices | grep "iPhone 16 Pro Max" | grep "Booted" | grep -E -o -i "([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})")
    
    # Update config.properties with simulator UDID
    sed -i '' "s/ios.simulator.udid=.*/ios.simulator.udid=$SIMULATOR_UDID/" src/test/resources/config.properties
    
    echo -e "${GREEN}iPhone 16 Pro Max simulator is already running with UDID: $SIMULATOR_UDID${NC}"
fi 