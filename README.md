Here's a draft for your README file based on the information you've provided:

---

# Strategic Routing and Charging: Minimizing Makespan In Electric Car Networks Through Game Theoretic Models

## Description

The problem is represented with a graph of vertices and edges, where vertices can signify the start positions of electric cars, charging stations (both fast and slow chargers), and end destinations. The edges are weighted, reflecting the time the car needs to travel between vertices. On the graph, a group of cars would travel to reach their end destinations (each represented with a unique ID). Every car needs to make exactly one stop at a charging station along its route. Charging takes a specified amount of time depending on the time the car has traveled and the type of charging station. In our case, the charging time will be a linear function. Every charging station can serve one car at a time. When a car arrives at an occupied charging station, the driver needs to wait until the charging station becomes free. This extends the overall travel time of the car. Thus, a careful choice of routes and charging stations along the routes can affect the overall travel time.

The code is written in Java.

## Installation Instructions

To set up this project locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Akita67/thesis.git
   ```
2. Navigate to the project directory:
   ```bash
   cd thesis/iterative/src
   ```
3. Compile the Java code:
   ```bash
   javac *.java
   ```
4. Run the project:
   ```bash
   java Main
   ```

## Usage

The project contains several classes and methods to simulate the routing and charging of electric cars in a network. The main classes and their functionalities are as follows:

### Classes

- **Agent**: Contains the information of every driver, with ID, current position, start, and end destination.
- **Vertex**: Contains the ID of the vertex and indicates if there is a charging station, and if so, whether it's a fast or a slow charging station.
- **Edge**: Represents the edges between two vertices and has a weight that represents the time needed to use that edge.
- **Graph**: Takes the edges and vertices together to create a graph where the agents (drivers) can travel. Also has the method `shortestPath` which is our greedy algorithm that defines the fastest roads that the agents will take to go to their charging stations/end destination.

### Methods

- **Main**: Takes the fastest road to the charging station without taking into account other players, exhibiting fully selfish behavior.
- **MainBrut**: The most advanced strategy, based on cooperation. It generates possible assignments in a certain amount of time, then prunes redundant assignments, keeping only unique ones, and checks if a Nash equilibrium has been reached.
- **NashEquilibrium**: Based on a starting seed (in our case, all agents initially take the charging station with ID 1), they will improve all together until they reach a Nash equilibrium.
- **Normal**: Exhibits normal behavior. If a charging station is empty, it is used. If the charging station is occupied but another charging station is available later, the next one is taken, spreading the drivers more evenly across the charging stations.

## Dependencies

This project requires Java to be installed on your system.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to customize any sections or add more details as needed! If you have any additional information or specific preferences, let me know, and I can adjust the README accordingly.
