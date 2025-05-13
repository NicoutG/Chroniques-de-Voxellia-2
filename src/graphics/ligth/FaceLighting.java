package graphics.ligth;

import graphics.shape.Face;

/**
 * Intensité / couleur reçues par chaque face visible d'un bloc.
 */
public final class FaceLighting {

    private ColorRGB left  = ColorRGB.BLACK;
    private ColorRGB right = ColorRGB.BLACK;
    private ColorRGB top   = ColorRGB.BLACK;

    /* accumulation additive (clampée dans [0-1]) */
    public void accumulate(Face f, ColorRGB c){
        switch (f) {
            case LEFT  -> left  = left.add(c).clamp();
            case RIGHT -> right = right.add(c).clamp();
            case TOP   -> top   = top.add(c).clamp();
        }
    }

    /* pour qu’un bloc source éclaire toutes ses faces à fond */
    public void inject(ColorRGB c){
        left = right = top = c.clamp();
    }

    /* ---------- getters attendus par Renderer ---------- */
    public ColorRGB left()  { return left;  }
    public ColorRGB right() { return right; }
    public ColorRGB top()   { return top;   }
}
