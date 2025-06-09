package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;
import world.World;

public class ObjectBehaviorText<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehavior<T,I,B> {
    
    protected final static String TEXT = "text";
    protected final static String TEXTS = "texts";
    private int index = 0;

    @Override
    public void onAttachTo(I objectInstance) {
        objectInstance.setState(TEXT, null);
        objectInstance.setState(TEXTS, new String[0]);
    }

    public void onInteraction(World world, I objectInstance) {
        String[] texts = getTexts(objectInstance);
        if (index >= texts.length) {
            objectInstance.setState(TEXT, null);
            index = 0;
        } else {
            objectInstance.setState(TEXT, texts[index]);
            index++;
        }
    }

    public String getText(I objectInstance) {
        Object state = objectInstance.getState(TEXT);
        if (state != null && state instanceof String)
            return (String) state;
        return null;
    }

    public String[] getTexts(I objectInstance) {
        Object state = objectInstance.getState(TEXTS);
        if (state != null && state instanceof String[])
            return (String[]) state;
        return null;
    }
    
}
