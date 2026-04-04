package objects.entity.entityBehavior.AI.utils;

import objects.entity.Entity;
import world.World;

@FunctionalInterface
public interface AICreator<T extends AI> {
    T create(World world, Entity entity);
}
