package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import http.TranslateHttp;
import moudle.TranslateBean;
import org.apache.http.util.TextUtils;
import util.Logger;

import java.awt.*;

public class TranslateAction extends AnAction {

    TranslateHttp http = new TranslateHttp();

    @Override
    public void actionPerformed(AnActionEvent e) {
        //Log 日志初始化
        Logger.init("translate" , Logger.DEBUG);

        //获取编辑器
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            Logger.info("mEditor == null");
            return;
        }

        //获取选择器
        SelectionModel model = mEditor.getSelectionModel();

        String selectText = model.getSelectedText();

        if (TextUtils.isEmpty(selectText)){
            Logger.info("selectText == null");
            return;
        }

        http.translate(selectText, new TranslateHttp.CallBack() {
            @Override
            public void callback(TranslateBean translateBean) {
                TranslateBean.WebBean webBean = translateBean.getWeb().get(0);

                //显示一个Popwindow
                showPopupBalloon(mEditor, webBean.getValue().toString());
            }
        });

    }

    /**
     * 显示一个Popwindow
     *
     * @param editor
     * @param result
     */
    private void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
