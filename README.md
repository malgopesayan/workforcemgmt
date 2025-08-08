# workforcemgmt
This project is an interactive dashboard built with Next.js and TypeScript. It allows users to visualize time-series data from the Open-Meteo API on an interactive map. Users can draw polygonal regions, which are then dynamically colored based on user-defined rules and data fetched for a specific time selected via a timeline slider.
 Screenshots
(Optional: Replace these placeholders with actual screenshots of your application)
Main Dashboard View	Polygon Drawing & Sidebar
![alt text](public/screenshot1.png)
![alt text](public/screenshot2.png)
✨ Core Features
Interactive Timeline: A 30-day horizontal slider to select a specific hour for data visualization.
Interactive Map: A Leaflet-based map where users can pan and define regions of interest.
Polygon Drawing: Users can draw polygons (3-12 vertices) directly on the map to define custom analysis zones.
Dynamic Data Fetching: Each polygon fetches hourly temperature data (temperature_2m) from the Open-Meteo API based on its geographic center.
Rule-Based Color Coding: Polygons are dynamically colored based on their current data value and a set of user-defined rules (e.g., < 10°C is blue, >= 25°C is red).
State Management: Centralized state management using Zustand ensures data is synchronized across all components (Map, Sidebar, Timeline).
Component-Based Architecture: Built with a clean and scalable React component structure.
Dynamic Updates: The entire dashboard reacts instantly to changes in the timeline, automatically fetching new data and updating the map visualization.
🛠️ Tech Stack & Libraries Used
This project was built using a modern, type-safe, and efficient stack:
Framework: Next.js (v14+ with App Router) - Provides a robust foundation with server components, file-based routing, and optimizations.
Language: TypeScript - For static typing, code quality, and maintainability.
Mapping:
React-Leaflet - React components for the powerful Leaflet map library.
react-leaflet-draw - A plugin for handling polygon drawing controls on the map.
State Management: Zustand - A small, fast, and scalable state-management solution with a minimal, hook-based API.
UI Components:
Ant Design - Used for UI elements like Cards, Buttons, and Tooltips to create a clean interface.
rc-slider - A flexible and customizable slider component for the timeline.
🚀 Setup and Run Instructions
To get a local copy up and running, follow these simple steps.
Prerequisites
Make sure you have Node.js (v18.x or later) and npm installed on your machine.
Node.js
Installation & Execution
Clone the repository:
code
Bash
git clone https://github.com/your-username/your-repo-name.git
Navigate to the project directory:
code
Bash
cd dynamic-data-dashboard
Install NPM packages:
code
Bash
npm install
Run the development server:
code
Bash
npm run dev
Open http://localhost:3000 with your browser to see the result.
📝 Design and Development Remarks
State Management Choice (Zustand): Zustand was chosen over Context API or Redux for its simplicity and minimal boilerplate. It provides a centralized store that can be accessed via hooks, which is a perfect fit for a project of this scale. It prevents prop-drilling and makes the data flow clean and predictable.
Component Architecture: The application is broken down into logical, reusable components located in src/components. This includes feature-specific folders (map, sidebar) and a general ui folder, promoting a clean separation of concerns.
Data Flow: The core reactive loop is managed by a useEffect hook in the main page.tsx. This hook listens for changes in the selectedTime (from the slider) or the polygons array. When a change is detected, it triggers API calls for all visible polygons and updates their state in the Zustand store, causing the UI to re-render with the new data and colors.
Handling SSR issues with Leaflet: Leaflet is a client-side library that directly interacts with the window object. To prevent errors during server-side rendering in Next.js, the InteractiveMap component is loaded dynamically with the ssr: false option.
API Abstraction: All logic for communicating with the Open-Meteo API is encapsulated in src/api/openMeteo.ts. This makes the code cleaner and would make it easy to add new data sources or caching mechanisms in the future.
