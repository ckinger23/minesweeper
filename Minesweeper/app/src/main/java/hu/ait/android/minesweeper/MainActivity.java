/*Carter King
 * Minesweeper
 * Professor Péter Ekler
 * June 13th, 2018*/


package hu.ait.android.minesweeper;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import hu.ait.android.minesweeper.model.MinesweeperModel;
import hu.ait.android.minesweeper.ui.MinesweeperView;

import com.facebook.shimmer.ShimmerFrameLayout;

import static hu.ait.android.minesweeper.model.MinesweeperModel.numBombs;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layoutContent;
    private TextView tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = findViewById(R.id.layoutContent);
        tvStatus = findViewById(R.id.tvStatus);

        final MinesweeperView minesweeperView = findViewById(R.id.minesweeperView);

        ToggleButton btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MinesweeperModel.getInstance().setFlagMode(true);//set it to where tap sets a flag
                } else {
                    MinesweeperModel.getInstance().setFlagMode(false);//try
                }
            }
        });

        ShimmerFrameLayout shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmerAnimation();
    }


    public void showMessage(String message) {
        Snackbar.make(layoutContent, message, Snackbar.LENGTH_LONG).show();
    }


    public void showFinalMessage(final String message) {
        final MinesweeperView minesweeperView = findViewById(R.id.minesweeperView);
        Snackbar.make(layoutContent, message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.new_game, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperView.resetGame();
                if (message == getString(R.string.you_lose)) {
                    changeTvStatus(getString(R.string.better_luck_msg));
                } else changeTvStatus(getString(R.string.win_again_msg));
            }
        }).show();

    }

    public void changeTvStatus(String message) {
        tvStatus.setText(message);
    }

    public void resetToggle() {
        final ToggleButton btnFlag = findViewById(R.id.btnFlag);
        btnFlag.setChecked(false);
    }
}









