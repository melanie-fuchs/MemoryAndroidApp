package ch.yumeart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatDrawableManager;


public class MemoryButtonImages extends AppCompatButton {

    private int row;
    private int column;
    private int frontImageDrawableID;
    private int cardDimension;

    protected boolean isFlipped;
    protected boolean isMatched;

    private Drawable front;
    private Drawable back;

    public MemoryButtonImages(Context context, int row, int column, int frontImageDrawableID, int cardDimension) {
        super(context);

        this.row = row;
        this.column = column;
        this.frontImageDrawableID = frontImageDrawableID;
        this.cardDimension = cardDimension;

        this.front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableID);
        this.back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.background);

        this.setBackground(back);

        // setting layout parameters for the view. Parent class will be GridLayout
        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));

        // Because not every device has the same size, the button's size (50 dp) will be multiplied by scale-factor
        tempParams.width = (int)getResources().getDisplayMetrics().density * cardDimension;
        tempParams.height = (int)getResources().getDisplayMetrics().density * cardDimension;
        tempParams.leftMargin = 5;
        tempParams.rightMargin = 5;
        tempParams.topMargin = 5;
        tempParams.bottomMargin = 5;

        this.setLayoutParams(tempParams);
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontImageDrawableID() {
        return frontImageDrawableID;
    }

    public void flip() {
        if(isMatched) {
            return;
        }

        if (isFlipped) {
            this.setBackground(back);
            this.isFlipped = false;
        } else {
            this.setBackground(front);
            this.isFlipped = true;
        }
    }
}
