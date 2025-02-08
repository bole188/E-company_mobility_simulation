# E-company Mobility Simulation

## Overview

E-company Mobility Simulation is a Java-based project that simulates the operations of an e-mobility rental company offering electric cars, bicycles, and scooters. The system handles vehicle management, rental processing, real-time movement simulation on a city map, and the generation of detailed financial reports and invoices.

## Key Features

- **Vehicle Management**
  - **Cars:** Manage vehicles with attributes such as unique ID, purchase date, price, manufacturer, model, description, and current battery level.
  - **Bicycles:** Store data including unique ID, manufacturer, model, purchase price, battery level, and range per charge.
  - **Scooters:** Maintain details like unique ID, manufacturer, model, battery level, purchase price, and maximum speed.
  - **Additional Functionality:** All vehicles can be charged, experience battery drainage during use, and record breakdowns (with description and timestamp).

- **Rental Processing**
  - Records rental transactions including date/time, user information, start and end locations, and rental duration (in seconds).
  - Cars require additional user documentation (passport or ID and driving license number).
  - Generates detailed TXT invoices that itemize base costs, distance-based charges (for narrow or wide city areas), discounts, promotions, and adjustments in case of breakdowns.

- **Cost Calculation**
  - Rental pricing is computed using configurable parameters stored in external properties files.
  - The calculation considers:
    - **Distance:** Charging different rates for narrow vs. wide city areas.
    - **Breakdowns:** Setting the cost to zero when a breakdown occurs.
    - **Discounts & Promotions:** Applying discounts on every 10th rental and additional promotional discounts.
    - **Base Prices:** Different per-vehicle pricing (e.g., `CAR_UNIT_PRICE`, `BIKE_UNIT_PRICE`, `SCOOTER_UNIT_PRICE`) multiplied by the rental duration.
  
- **Real-Time Simulation**
  - Simulates each rental in its own thread, moving vehicles along a direct, linear path on a 20x20 grid map.
  - The map distinguishes between narrow (blue) and wide (white) areas.
  - Movement is visualized in real-time, with vehicles displaying their ID and battery level on the map.

- **Business Reporting**
  - **Summary Reports:** Aggregate data including total revenue, discounts, promotions, ride amounts in narrow/wide areas, maintenance costs, repair costs (calculated using type-specific coefficients), overall company expenses, and tax amounts.
  - **Daily Reports:** Similar data as the summary reports, grouped by date.
  - **Additional Analysis:** Identification of vehicles with the highest revenue, highest losses, or most expensive repairs, with serialized data available for further inspection.

- **Graphical User Interface (GUI)**
  - Multiple GUI screens are implemented using JavaFX or Swing:
    - **Main Map Screen:** Displays the city grid and real-time vehicle movement.
    - **Vehicle Overview:** Shows detailed tables for cars, bicycles, and scooters.
    - **Breakdown Reports:** Lists breakdown events with vehicle type, ID, timestamp, and description.
    - **Business Results:** Presents both summary and daily business reports.
    - **Serialization Viewer:** Allows users to deserialize and view pre-serialized vehicle data.

## Project Structure

The project is structured into several packages within the `/src` folder, ensuring modularity and clear separation of concerns:

- **`model`**
  - Contains entity classes representing the different types of vehicles (`Car`, `Bicycle`, `Scooter`), rental transactions, invoices, and possibly breakdown records.

- **`simulation`**
  - Houses classes that manage the simulation of vehicle movements on the map.
  - Handles the creation and management of threads for each rental, simulating real-time navigation across the grid.

- **`gui`**
  - Implements the graphical user interfaces using JavaFX or Swing.
  - Manages displays for the map, vehicle data, breakdown logs, and financial reports.

- **`utils`**
  - Provides helper classes for:
    - Reading and parsing configuration from properties files.
    - Performing cost calculations and financial aggregations.
    - Managing serialization and deserialization of objects.
    - Logging and other common utilities.

## Configuration

- **Properties Files:**  
  The project utilizes external properties files to define various parameters such as:
  - Rental unit prices (`CAR_UNIT_PRICE`, `BIKE_UNIT_PRICE`, `SCOOTER_UNIT_PRICE`).
  - Distance multipliers for narrow and wide city areas.
  - Discount and promotion percentages.
  - Paths for input data and generated invoices.

## Setup and Execution

1. **Build the Project:**
   - The project is built using standard Java build tools (such as Maven or Gradle). Ensure that all dependencies are correctly configured.

2. **Prepare Data:**
   - Rental and vehicle data are provided via files (accessible, for example, through Moodle). Verify that these files are placed as specified by the configuration properties.

3. **Run the Application:**
   - Execute the main application class from the `/src` directory to launch the simulation and display the GUI.
   - The simulation will process rental transactions in chronological order, update the map in real time, and generate invoices and reports accordingly.

4. **Generate Documentation:**
   - JavaDoc comments are provided throughout the code. Generate the documentation to review the API details and code structure.

## Conclusion

This project demonstrates a comprehensive simulation of an e-mobility rental service, integrating real-time simulation, multithreaded rental processing, dynamic GUI updates, and detailed financial reporting. Its modular design, reliance on external configuration, and adherence to best coding practices (such as proper package organization and code reusability) make it a robust example of a modern Java application.

For more details, explore the `/src` folder and review the JavaDoc-generated documentation.
