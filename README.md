# Planet-Fireworks

This program was created as a little project on New Year's Eve. It shows a small procedural generated earth-like planet from which fireworks are launched into the air. It is written in Java and uses LWJGL to access OpenGL.

## Table of contents
1. [ System Requirements ](#system)
2. [ Build ](#build)
3. [ Controls ](#controls)
4. [ Information about the code and program flow ](#code)
5. [ Results ](#results)
6. [ References ](#references)

<a name="system"></a>
## System Requirements
- Java Version 14 or higher (otherwise small rewrites are necessary to undo new language features)
- OpenGL 3.3 or higher
- Maven

I have tested the program on a Linux system. In the maven configuration file (pom.xml) Windows and MacOS are configured as well and might work.

<a name="build"></a>
## Build
1. Compile with `mvn compile`.
2. Execute with `mvn exec:java -Dexec.mainClass="planet.PlanetRender"`.

(Or use a Java IDE with Maven support...)

<a name="controls"></a>
## Controls
1. Movement and Camera:
   - W,A,S,D,Shift,Space: Move the camera forward,left,backward,right,down,up.
   - Left mouse button (press and hold) and dragging the mouse: Rotate the camera.
   - Mouse wheel: Zoom the camera.
2. Other:
   - T (press and hold): Render as wireframe (only edges of the mesh visible).

<a name="code"></a>
## Information about the code
1. The package `src/main/java/planet/` contains all the classes directly related to the construction and simulation of the planets and fireworks.
2. The package `src/main/java/renderengine/` contains the classes needed to communicate with and access OpenGL.
3. The resource folder `src/main/resources/shaders/` contains the vertex and fragment shaders for the program. Especially:
   - `planet_vert.glsl` and `planet_frag.glsl` are the vertex and fragment shader to render the planets.
   - `firework_vert.glsl` and `firework_frag.glsl` are the vertex and fragment shader to render the fireworks.

<a name="results"></a>
## Results
![brighter](https://user-images.githubusercontent.com/34870366/147842148-0ad1f046-f1d4-418a-baa2-260af4f6ede9.png)
![sun](https://user-images.githubusercontent.com/34870366/147842123-7d2a3081-52c1-4776-a87b-8e454ab242b4.png)

https://user-images.githubusercontent.com/34870366/147842153-4105554d-e973-4273-9028-4cb6d011b58e.mp4


<a name="references"></a>
## References
*Main* OpenGL and LWJGL references that I have used:
- https://learnopengl.com/
- Tutorial series "OpenGL 3D Game Tutorial" by https://www.youtube.com/user/ThinMatrix
- https://www.lwjgl.org/guide
