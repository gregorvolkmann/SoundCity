# SoundCity
A project I worked on with Denis Seyfried and Onur Karaman for the lecture *Development of Interactive User Interfaces* at Hochschule RheinMain.

Runs on jMonkeyEngine 3.0

# Documentation (German)

## Grundidee
Die Grundidee von SoundCity ist eine 2D Simulation mit Schwerpunkt auf Audio. Der Spielstil und die Steuerung soll aus Vogelperspektive in etwa wie GTA oder GTAII ablaufen. Der Spieler sieht sich selbst im Mittelpunkt der Karte und kann sich frei auf einer von uns erstellen Karte bewegen. Bei der Klangsimulation geht es um räumliche und natürliche Nachahmung einer virtuellen Umgebung. Beispiele hierfür sind Geräusche durch Personen (NPCs), Tieren oder Straßenlärm. Es soll ein dreidimensionales Klangbild entstehen, welches wir mit Hilfe des Dopplereffekts nachbilden.

SoundCity wird in Java entwickelt und basiert auf dem Lightweigtht Java Game Library (LWJGL) Framework, welches uns die nötigen Schnittstellen OpenGL, OpenAL und OpenCL für die Entwicklung eines Spiels liefert.

In erster Instanz soll das Projekt die Basis für später größere Projekte bieten. SoundCity wird ein kleines Spiel, bei dem es das Ziel ist alle Musiknoten, welche per Zufall auf der Karte positioniert werden, einzusammeln. Es ist wichtig diese in dem vorgegebenen Zeitraum einzusammeln und den Stresspegel möglichst niedrig zu halten. Der Stresspegel ist eine Art HP­Balken (Hit Points), welcher sich aber automatisch regenerieren kann, wenn man sich in bestimmen Ruhezonen ausruht. Schafft man es nicht, die nächste Musiknote rechtzeitig einzusammeln, wird der Song abgebrochen und man hat verloren. Schafft man es die Note einzusammeln, wird das Lied um eine festgelegte Länge verlängert.

Geplant waren NPCs und Objekte, welche auf der Straße den Stresspegel des Spielers erhöht um somit eine gewisse Schwierigkeitsstufe in das Spiel hineinzubringen.

## Planung und Themen
Die Aufgaben zur Entwicklung wurden wie folgt unterteilt:

* 3D-Modellierung
* Steuerung und Kameraführung
* Kollision mit Objekten
* Audio
* HUD-Interface
* Spawnpunkte für Musiknoten
* Stresspegel
* Weitere Ideen

## Wichtige Anmerkung
Um das Spiel in einer Testebene auszuführen, muss jMonkeyEngine3 installiert werden. Das Spiel kann in einer maximalen Auflösung von 1152x864 ausgeführt werden.

## 3D-Modellierung
Blender ist eine freie (mit der GPL lizenzierte) 3D-Grafiksoftware. Sie enthält Funktionen, um dreidimensionale Körper zu modellieren, sie zu texturieren, zu animieren und zu rendern.1 Die Karte, die NPC-Models, das Playermodel und das Model der Musiknote haben wir mit blender modelliert, da die jMonkeyEngine sehr gut und flexibel mit Dateiformat *.blend arbeiten kann.

## Steuerung und Kameraführung
Die Kameraführung ist fest auf den Player gerichtet und folgt ihm aus der Vogelperspektive. So ist der Player immer im Mittelpunkt der Kamera. Sämtliche Kameraeinstellungen werden bereits von der Library geliefert und müssen nur noch entsprechend angepasst werden, sodass sie unseren Wünschen für unser Spiel entspricht.

Die Kamera lässt sich während des Spielens mit gedrückter linker Maustaste verstellen um die Ansicht zu verändern.

W - Geradeaus bewegen
A - Linksdrehung
D - Rechtsdrehung
S - Rückwärts bewegen

**Sondertasten:**
M - Ton muten
L - Debugmode

Via dem inputManager kann man einfach alle Tasten festlegen, welche verwendet werden.

    private void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A)); inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));      
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W)); inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S)); 
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE)); ...
        inputManager.addListener(this,"Left");
        inputManager.addListener(this,"Right");
        inputManager.addListener(this,"Up");
        inputManager.addListener(this,"Down");
        inputManager.addListener(this,"Space");
        ...
    }

Jede Taste kann belegt werden, muss aber in setupKeys vordefiniert werden, sodass man einen Tastenanschlag überhaupt abfangen kann. Über einen onActionListener werden dann die Tastenanschläge gefangen und eine entsprechende Methode ausgeführt.

    public void onAction(String key, boolean pressed, float tpf) {
        if (key.equals("Left")) { left = pressed; }
        else if (key.equals("Right")) { right = pressed; }
        else if (key.equals("Up")) { up = pressed; }
        else if (key.equals("Down")) { down = pressed; }
        ...
    }
