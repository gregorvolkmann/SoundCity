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

## Kollision mit Objekten
Die Kollision von Objekten wird über unseren ColManager verwaltet. Es wird geprüft ob der Player mit Objekten (dem Level), NPCs oder Musiknoten in Berührung kommt. Dabei wird Grundauf wird mit den Nodes gearbeitet, sodass eine physikalische Kollision überhaupt festgestellt werden kann. Essentiell wichtig ist natürlich das Einsammeln der Noten, welches einfach durch die Schnittmenge von Objekten ermittelt werden kann. So verursachen die NPCs bei Berührung “Schaden”, welches den Stresspegel erhöht.

## SoundFactory
Die SoundFactory generiert SoundFragmente. Jedes Fragment setzt sich aus einem Model und einem physikalischen Körper zusammen. Das Model ist das für den Benutzer sichtbare Fragment und der Körper die dazugehörigen physikalischen Eigenschaften. Die Factory verwaltet zudem die Spawnpunkte der einzelnen Fragmente, sodass die Fragmente nicht ineinander gespawned werden.

## Audio
JmonkeyEngine3 bietet OpenAL und unterstützt .ogg und .wav Audiodateien. Es sind alle nötigen Methoden gegeben um AudioNodes in die Welt zu platzieren und somit auch einen 3D-Raumklang zu erzeugen. Für unsere Zwecke brauchen wir Backgroundmusik und Objekte die Geräusche von sich geben. Etwa beim Kollidieren mit NPCs oder beim Einsammeln von Musiknoten.

Die Library bietet zahlreiche Methoden zur Steuerung und Handhabung von Audioelementen. Somit könnten wir auch beim Bewegen des Players Schrittgeräusche erzeugen solange man eine der vier Steuerungstasten (W, A, S, D) drückt.

Für Audiogeräusche und Soundtracks haben wir uns an freien Musterstücken aus dem Internet bedient und dann mit einem Encodeprogramm in ein kompatibles Format gebracht.

Jedes AudioNode hat einen Dateipfad und eine Lautstärke Variable. Die BGM hat dazu noch zwei boolsche Werte die angeben, dass es sich auch um eine BGM dreht oder ob es endlos im Loop abgespielt werden soll. Typische andere AudioNodes werden bei Kollision oder Events wie z.B. das Verlassen der Karte getriggert. Ähnlich vergleichbar wie in Egoshootern, dass beim Drücken der linken Maustaste die Waffe schießt und solange Geräusche verursacht bis man wieder loslässt oder das Magazin leer ist. Diese Optionen bietet uns die Engine ebenso.

## HUD-Interface
Das HUD-Interface (Head-up-Display) ist eine eigene Klasse die alle wichtigen Informationen (und das Logo von SoundCity) im Vordergrund auf oberster Ebene dargestellt wird. Im HUD sieht man, wieviele Musiknoten man bereits eingesammelt hat und wieviele noch verbleiben. Der Stresspegel wird auch als eine Leiste dargestellt und zeigt wie lange man noch herumlaufen kann, bis man sich erstmal etwas ausruhen muss um den Stresspegel wieder auf 0 zu senken. Die verbleibende Zeit bis zum Einsammeln der nächsten Note ist wichtig, denn der Spieler muss wissen, wie viel Zeit er noch hat und somit ein Game Over zu vermeiden.

## Spawnpunkte für Musiknoten
Die Nodes für die Musiknoten werden in der SoundFactory generiert. Allerdings werden die meisten Werte für das Objekt bereits in der Factory festgelegt, da einfach gewisse Vorgaben sein müssen. Die Spawnpunkte der Musiknoten sollten eigentlich zufällig platziert werden. Dabei entsteht aber das Problem, dass die Musiknoten theoretisch auch in Strukturen gespawnt werden können und somit nicht mehr zugänglich sind.

Theoretisch muss beim Spawnen immer auf zweidimensionaler Ebene die Fläche der bereits erstellten und zu erstellenden Objekte betrachtet werden. Schneidet die zu erstellende Musiknote ein Gebäude oder befindet sich die Fläche der Note teils oder ganz innerhalb einer Struktur, muss eine neue Position ermittelt werden, solange bis sie nicht in einer Struktur gespawnt wird.

## Stresspegel
Der Stresspegel ist vergleichbar wie ein Hitpoint­Wert oder noch genauer eine Art Ausdauerleiste, wie lange man sich frei den NPCs und anderen Geräuschen aussetzen kann. Der Stresslevel wird immens erhöht, wenn man das Level verlässt oder mit einem NPC kollidiert. Also sollte man möglich vermeiden die Karte zu verlassen und darauf zu achten allen NPCs auszuweichen. Der Stresspegel wird in Form einer Leiste im HUD angezeigt und vermittelt immer den aktuellen Stand des Stresspegels. Es ist mit dem Playernode verbunden und kann so einfach bei Kollision erhöht werden.

Es funktioniert nach folgendem Algorithmus:
Die Collision­Klasse läuft mit einer Integer-Variable, die alle Kollisionen zwischen Player und NPC zusammenzählt. Da pro Sekunde mehrere Kollisionabfragen stattfinden und sich somit die Klassenvariable in diesem Fall verhundertfacht, falls eine “Stresskollision” stattfindet, wird die aktuelle Anzahl der Kollision (Bsp. 150) durch 10 zurückgegeben und dies beim SimpleUpdate() des Spiels für >3 überprüft. Ist dies der Fall, wird die Stressbar vergrößert und die Klassenvariable der Kollision wird wieder zurückgesetzt.

## JmonkeyEngine3
 jMonkeyEngine (auch Java Monkey Engine oder jME) ist eine Szenengraph­basierte und komplett in Java geschriebene Grafik­API. Viele der Ideen, die in jME verwirklicht wurden
stammen aus dem Buch "3D Game Engine Design" von David Eberly.2
Die Engine bietet uns eine umfangreiche Library von Methoden für den Einsatz von Spiele. Dank der Online­Doku ist es einfach etwas zu finden und sich mit der Engine vertraut zu machen, perfekt für unser Spiel.

## Weitere Ideen
SoundCity ist eine Umsetzung unserer Initialideen und kann natürlich wunderbar erweitert und noch weiter ausgefeilt und optimiert werden. So könnten in Zukunft auch noch Laufanimationen für NPCs und das Playermodel eingefügt werden.
Ein Startmenü, bevor das Spiel überhaupt beginnt, wo man entsprechende Einstellungen vornehmen kann und sogar einen eigenen Song oder eine ganze Playlist von Songs vordefiniert laden könnte. Auch wären noch mehrere Level oder eine größere Welt mit noch mehr Hindernissen und lustigen Elementen wie z.B. einen Laufgeschwindigkeitsbeschleuniger oder gar Objekte die einem die Steuerung verdrehen und dann behindern. Kreativität lässt seinen freien Lauf und SoundCity bietet noch viele Möglichkeiten.

## Programme und Werkzeuge
blender - 3D Modellierung
JmonkeyEngine3 - Java Programmierung
freac - Kodierung von Audiodateien
asana - Planung von Aufgaben
Illustrator - Grafiken zur Visualisierung
Photoshop - Logo und Icons

## Externes Material
Musik von SoundCloud
Soundeffekte von freien Quellen im Internet

## Quellen
http://de.wikipedia.org/wiki/Lightweight_Java_Game_Library http://www.lwjgl.org
http://jmonkeyengine.org/wiki/doku.php/
