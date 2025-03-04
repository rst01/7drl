package roguelike.entities;

import roguelike.world.World;

import java.util.Map;
import java.util.Random;

public class Creature extends Entity {

    private int hitpoints;
    private String behaviour;

    public Creature(Map<String, String> creatureData, int x, int y) {
        super(creatureData, x, y);
        behaviour = creatureData.get("behaviour");
        hitpoints = 10;
    }

    private int getHitpoints() {
        return hitpoints;
    }

    private void setHitpoints(int amount, World world) {
        hitpoints += amount;
        if (hitpoints <= 0) {
            world.removeEntity(this);
        }
    }

    public boolean isDead() {
        return hitpoints <= 0;
    }


    public void move(World world, int dx, int dy)
    {
        if (world.isBlocked(x + dx, y + dy) != true)
        {
            x += dx;
            y += dy;
        } else {
            Entity entity = world.getEntityAt(x + dx, y + dy);

            if (entity instanceof Item) {
                useItem((Item) entity);
            } else if (entity instanceof Creature) {
                attackCreature((Creature) entity, world);
            }
        }
    }

    public void useItem(Item item) {
        if (item.getEffect() == "health" && hitpoints <= 90) {
            hitpoints += 10;
        }
    }

    public void attackCreature(Creature creature, World world) {
        creature.setHitpoints(-50, world);
        if (creature.getHitpoints() <= 0) {
            world.removeEntity(creature);
        }
    }

    public void moveTo(World world, int x, int y)
    {
        //
    }

    public void update(World world)
    {
        Random rnd = new Random();
        int performAction = rnd.nextInt(100);
        if (behaviour.equals("docile") && performAction > 98) {
            // walk around and flee if attacked

            int rndNr = rnd.nextInt(3);

            if (rndNr == 0) {
                move(world, 1, 0);
            } else if (rndNr == 1) {
                move(world, -1, 0);
            } else if (rndNr == 2) {
                move(world, 0, 1);
            } else if (rndNr == 3) {
                move(world, 0, -1);
            }

        } else if (behaviour.equals("aggressive") && performAction > 98) {
            int awarenessDistance = 5;
            Creature player = world.player;

            int dx = player.getX() - this.x;
            int dy = player.getY() - this.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= awarenessDistance) {
                int stepX = 0;
                int stepY = 0;

                    if (dx > 0) {
                    stepX = 1;
                } else if (dx < 0) {
                    stepX = -1;
                }

                if (dy > 0) {
                    stepY = 1;
                } else if (dy < 0) {
                    stepY = -1;
                }

                move(world, stepX, stepY);
            }
        }

    }
}