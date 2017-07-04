package tool.xfy9326.floattext.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.Method.QuickStartMethod;
import tool.xfy9326.floattext.Utils.App;

@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingService extends TileService {

    @Override
    public void onStartListening() {
        StateChange();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        App utils = (App) getApplicationContext();
        if (tile.getState() == Tile.STATE_ACTIVE) {
            if (utils.StartShowWin) {
                tile.setState(Tile.STATE_INACTIVE);
                FloatManageMethod.ShutDown(this);
            } else {
                tile.setState(Tile.STATE_INACTIVE);
            }
        } else if (tile.getState() == Tile.STATE_INACTIVE) {
            if (utils.StartShowWin) {
                tile.setState(Tile.STATE_ACTIVE);
            } else {
                if (QuickStartMethod.Launch(this)) {
                    tile.setState(Tile.STATE_ACTIVE);
                }
            }
        }
        tile.updateTile();
        super.onClick();
    }

    private void StateChange() {
        App utils = (App) getApplicationContext();
        Tile tile = getQsTile();
        if (utils.StartShowWin) {
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

}
