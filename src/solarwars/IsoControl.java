/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: IsoControl.java
 * Type: solarwars.IsoControl
 * 
 * Documentation created: 15.03.2012 - 20:36:19 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import entities.AbstractPlanet;
import entities.ShipGroup;
import logic.ActionLib;
import logic.Gameplay;

/**
 * The Class IsoControl.
 */
public class IsoControl {

	/** The root node. */
	private Node rootNode;
	
	/** The shootables node. */
	private Node shootablesNode;

	/**
	 * Gets the shootables node.
	 *
	 * @return the shootables node
	 */
	public Node getShootablesNode() {
		return shootablesNode;
	}

	/** The marker node. */
	private Node markerNode;
	
	/** The last node. */
	private Node lastNode;
	
	/** The marker. */
	private ParticleEmitter marker;
	
	/** The geometry. */
	private Geometry geometry;
	
	/** The cam. */
	private Camera cam;
	
	/** The action listener. */
	private ActionListener actionListener;
	
	/** The action lib. */
	private ActionLib actionLib;

	/**
	 * Gets the action listener.
	 *
	 * @return the action listener
	 */
	public ActionListener getActionListener() {
		return actionListener;
	}

	/**
	 * Instantiates a new iso control.
	 *
	 * @param assetManager the asset manager
	 * @param rootNode the root node
	 * @param cam the cam
	 * @param inputManager the input manager
	 */
	public IsoControl(AssetManager assetManager, Node rootNode, Camera cam,
			InputManager inputManager) {
		this.rootNode = rootNode;
		this.cam = cam;
		this.actionLib = ActionLib.getInstance();

		markerNode = new Node("Marker Transform");

		Quad q = new Quad(1, 1);

		geometry = new Geometry("MarkerGeometry", q);
		Material material = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap",
				assetManager.loadTexture("Textures/gui/marker.png"));
		// material.setColor("Color", new ColorRGBA(0, 0, 0, 0));

		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		geometry.setMaterial(material);

		/**
		 * Objects with transparency need to be in the render bucket for
		 * transparent objects:
		 */
		geometry.setQueueBucket(Bucket.Translucent);

		float angles[] = { (float) -Math.PI / 2, (float) -Math.PI / 2, 0 };

		// geometry.setLocalTranslation(-0.5f, 0, -0.5f);
		geometry.setLocalRotation(new Quaternion(angles));

		markerNode.attachChild(geometry);

		// /** Explosion effect. Uses Texture from jme3-test-data library! */
		// marker = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle,
		// 1);
		// Material debris_mat = new Material(assetManager,
		// "Common/MatDefs/Misc/Particle.j3md");
		// debris_mat.setTexture("Texture",
		// assetManager.loadTexture("Textures/Effects/marker.png"));
		// marker.setMaterial(debris_mat);
		// marker.setImagesX(1);
		// marker.setImagesY(1); // 3x3 texture animation
		// marker.setStartSize(0.5f);
		// marker.setEndSize(0.5f);
		// marker.setLowLife(0.18f);
		// marker.setHighLife(0.18f);
		//
		// // marker.setLowLife(0.55f);
		// // marker.setHighLife(0.6f);
		//
		// marker.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0,
		// 0));
		// marker.setStartColor(new ColorRGBA(0.1f, 0.1f, 1f, 1f));
		// marker.setEndColor(new ColorRGBA(0.1f, 0.1f, 1f, 0.3f));
		// //debris.getParticleInfluencer().setGravity(6f);
		// //marker.getParticleInfluencer().setVelocityVariation(.60f);
		// markerNode.attachChild(marker);
		// marker.emitAllParticles();

		initialize(rootNode, inputManager);
	}

	/**
	 * Initializes the.
	 *
	 * @param rootNode the root node
	 * @param inputManager the input manager
	 */
	private void initialize(final Node rootNode, final InputManager inputManager) {
		shootablesNode = new Node("Shootables");
		rootNode.attachChild(shootablesNode);
		// Defining the "Shoot" action: Determine what was hit and how to
		// respond.
		actionListener = new ActionListener() {

			public void onAction(String name, boolean keyPressed, float tpf) {

				int action = -1;

				// LEFT CLICK = 1
				if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
					action = 1;
				} // RIGHT CLICK = 2
				else if (name
						.equals(SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK)) {
					action = 2;
				} // WHEEL DOWN
				else if (name
						.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_DOWN)) {
					action = 3;
				} // WHEEL UP
				else if (name
						.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_UP)) {
					action = 4;
				}

				if ((action == 1 || action == 2) && !keyPressed) {
					// 1. Reset results list.
					CollisionResults results = new CollisionResults();
					// 2. calculate ray from camera to mouse pointer
					Vector2f click2d = inputManager.getCursorPosition();
					Vector3f click3d = cam.getWorldCoordinates(
							new Vector2f(click2d.x, click2d.y), 0f).clone();
					Vector3f dir = cam
							.getWorldCoordinates(
									new Vector2f(click2d.x, click2d.y), 1f)
							.subtractLocal(click3d).normalizeLocal();
					Ray ray = new Ray(click3d, dir);
					// 3. Collect intersections between Ray and Shootables in
					// results list.
					shootablesNode.collideWith(ray, results);
					// 4. Print the results
					System.out.println("----- Collisions? " + results.size()
							+ "-----");
					for (int i = 0; i < results.size(); i++) {
						// For each hit, we know distance, impact point, name of
						// geometry.
						float dist = results.getCollision(i).getDistance();
						Vector3f pt = results.getCollision(i).getContactPoint();
						String hit = results.getCollision(i).getGeometry()
								.getName();
						System.out.println("* Collision #" + i);
						System.out.println("  You shot " + hit + " at " + pt
								+ ", " + dist + " wu away.");
					}
					// 5. Use the results (we mark the hit object)
					if (results.size() > 0) {
						// The closest collision point is what was truly hit:
						CollisionResult closest = results.getClosestCollision();

						// Let's interact - we mark the hit with a red dot.
						// markerNode.setLocalTranslation(closest.getContactPoint());

						lastNode = closest.getGeometry().getParent();

						AbstractPlanet p = null;
						ShipGroup sg = null;
						Node n = lastNode.getParent();

						if (n instanceof AbstractPlanet) {
							p = (AbstractPlanet) n;

							if (action == 1) {
								actionLib.invokePlanetAction(null, p,
										Hub.getLocalPlayer(),
										Gameplay.PLANET_SELECT);

								repositMarker(p);

							} else if (action == 2) {
								actionLib.invokePlanetAction(null, p,
										Hub.getLocalPlayer(),
										Gameplay.PLANET_ATTACK);
							}
						} else if (n instanceof ShipGroup) {
							if (action == 1) {
								sg = (ShipGroup) n;
								repositMarker(sg);
								actionLib.invokeShipAction(null, sg,
										Hub.getLocalPlayer(),
										Gameplay.SHIP_REDIRECT);
							}
						}

					} else {
						// No hits? Then remove the mark.
						if (lastNode != null) {
							lastNode.detachChild(markerNode);
						}
						// rootNode.detachChild(markerNode);
					}
				}

				if (action == 3 || action == 4) {
					float percentage = Hub.getLocalPlayer().getShipPercentage();
					if (action == 3) {
						percentage += 0.025f;
					} else {
						percentage -= 0.025f;
					}
					Hub.getLocalPlayer().setShipPercentage(percentage);
				}
			}
		};
	}

	/**
	 * Reposit marker.
	 *
	 * @param p the p
	 */
	private void repositMarker(AbstractPlanet p) {
		if (lastNode != null) {
			lastNode.detachChild(markerNode);
		}

		lastNode.attachChild(markerNode);

		float s = p.getSize() * 2.6f;

		geometry.setLocalScale(s);
		geometry.setLocalTranslation(-s / 2, 0, -s / 2);
		// marker.killAllParticles();
		// marker.setStartSize(p.getSize() + 0.2f);
		// marker.setEndSize(p.getSize() + 0.2f);
		// marker.emitAllParticles();
	}

	/**
	 * Reposit marker.
	 *
	 * @param g the g
	 */
	private void repositMarker(ShipGroup g) {
		if (lastNode != null) {
			lastNode.detachChild(markerNode);
		}

		lastNode.attachChild(markerNode);

		float s = g.getSize() * 2.0f;

		geometry.setLocalScale(s);
		geometry.setLocalTranslation(-s / 2, 0, -s / 2);
		// marker.killAllParticles();
		// marker.setStartSize(g.getSize()*8 + 0.2f);
		// marker.setEndSize(g.getSize()*8 + 0.2f);
		// marker.emitAllParticles();
	}

	/**
	 * Adds the shootable.
	 *
	 * @param spat the spat
	 */
	public void addShootable(Spatial spat) {
		shootablesNode.attachChild(spat);
	}

	/**
	 * Removes the shootable.
	 *
	 * @param spat the spat
	 */
	public void removeShootable(Spatial spat) {
		shootablesNode.detachChild(spat);
	}
}
