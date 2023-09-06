package br.net.pin.qin_sunwiz.mage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class WizDesk {

    private static boolean started = false;
    private static String title = null;

    public static void start(String title) throws Exception {
        java.awt.EventQueue.invokeAndWait(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                WizDesk.started = true;
                WizDesk.title = title;
            } catch (Exception e) {
                WizLog.erro(e);
            }
        });
    }

    public static boolean isStarted() {
        return WizDesk.started;
    }

    public static void message(String message) {
        WizDesk.message(message, false);
    }

    public static void message(String message, boolean silent) {
        WizLog.info(message);
        if (!silent) {
            Runnable runner = () -> {
                JOptionPane.showMessageDialog(WizDesk.getActiveWindow(), message,
                                WizDesk.title,
                                JOptionPane.INFORMATION_MESSAGE);
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runner.run();
            } else {
                SwingUtilities.invokeLater(runner);
            }
        }
    }

    public static boolean question(String question) {
        return JOptionPane.showConfirmDialog(WizDesk.getActiveWindow(), question,
                        WizDesk.title,
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static String input(String question, String value) {
        return (String) JOptionPane.showInputDialog(WizDesk.getActiveWindow(), question,
                        WizDesk.title,
                        JOptionPane.QUESTION_MESSAGE, null, null, value);
    }

    public static Window getActiveWindow() {
        for (Window old : Window.getWindows()) {
            if (old.isActive()) {
                return old;
            }
        }
        return null;
    }

    public static void setNextLocationFor(Window window) {
        Point result = null;
        var active = WizDesk.getActiveWindow();
        if (active != null) {
            result = new Point(active.getX() + 45, active.getY() + 45);
        }
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (result == null) {
            result = new Point(WizRand.getInt(screen.width - window.getWidth()),
                            WizRand.getInt(screen.height - window.getHeight()));
        } else {
            if (result.x + window.getWidth() > screen.width) {
                result.x = screen.width - window.getWidth();
            }
            if (result.y + window.getHeight() > screen.height) {
                result.y = screen.height - window.getHeight();
            }
        }
        window.setLocation(result);
    }

    public static JPanel wrap(JComponent component, String title) {
        var result = new JPanel(new BorderLayout(0, 0));
        result.add(new JLabel(title), BorderLayout.NORTH);
        result.add(component, BorderLayout.CENTER);
        return result;
    }

    public static void setWidthMinAsPreferredMax(JComponent... ofComponents) {
        var maxValue = 0;
        for (JComponent component : ofComponents) {
            if (component.getPreferredSize().width > maxValue) {
                maxValue = component.getPreferredSize().width;
            }
        }
        for (JComponent component : ofComponents) {
            var dimension = new Dimension(maxValue, component.getPreferredSize().height);
            component.setMinimumSize(dimension);
            component.setPreferredSize(dimension);
        }
    }

    public static void putShortCut(JComponent component, String name, String keyStroke,
                    Runnable runnable) {
        var inputMap = component.getInputMap(
                        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        var actionMap = component.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyStroke), name);
        actionMap.put(name, WizDesk.getAction(runnable));
    }

    public static Action getAction(Runnable runnable) {
        return new AbstractAction() {
            private static final long serialVersionUID = -1482117853128881492L;

            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }
        };
    }

    public static String getStringFromClipboard() throws Exception {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(
                        DataFlavor.stringFlavor);
    }

    public static void copyToClipboard(String theString) {
        var selection = new StringSelection(theString);
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static BufferedImage getImageFromClipboard() throws Exception {
        var transferable = Toolkit.getDefaultToolkit().getSystemClipboard()
                        .getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(
                        DataFlavor.imageFlavor)) {
            var pasted = (BufferedImage) transferable.getTransferData(
                            DataFlavor.imageFlavor);
            return WizDesk.convertToRGB(pasted);
        }
        return null;
    }

    public static BufferedImage convertToRGB(BufferedImage image) {
        var result = new BufferedImage(image.getWidth(), image.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    public static Pattern deskMnemonic = Pattern.compile("\\s\\s\\s\\[\\s.\\s\\]$");

    public static String delMnemonic(String fromTitle) {
        if (fromTitle == null) {
            return fromTitle;
        }
        if (WizDesk.deskMnemonic.matcher(fromTitle).find()) {
            return fromTitle.substring(0, fromTitle.length() - 8);
        }
        return fromTitle;
    }

    public static JComponent getItem(JMenuBar fromBar, String onPath) {
        JComponent result = fromBar;
        if (onPath != null) {
            var parts = onPath.split("\\.");
            if (parts.length > 0) {
                for (String part : parts) {
                    if (result instanceof JMenu) {
                        var found = false;
                        for (Component comp : ((JMenu) result).getMenuComponents()) {
                            if (comp instanceof JMenu) {
                                if (part.equals(WizDesk.delMnemonic(((JMenu) comp)
                                                .getText()))) {
                                    result = (JMenu) comp;
                                    found = true;
                                }
                            } else if (comp instanceof JMenuItem) {
                                if (part.equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                                .getText()))) {
                                    result = (JMenuItem) comp;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            return null;
                        }
                    } else {
                        var found = false;
                        for (Component comp : result.getComponents()) {
                            if (comp instanceof JMenu) {
                                if (part.equals(WizDesk.delMnemonic(((JMenu) comp)
                                                .getText()))) {
                                    result = (JMenu) comp;
                                    found = true;
                                }
                            } else if (comp instanceof JMenuItem) {
                                if (part.equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                                .getText()))) {
                                    result = (JMenuItem) comp;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            return null;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static JComponent getItem(JPopupMenu doPopup, String onPath) {
        JComponent result = doPopup;
        if (onPath != null) {
            var parts = onPath.split("\\.");
            if (parts.length > 0) {
                for (String part : parts) {
                    if (result instanceof JMenu) {
                        var found = false;
                        for (Component comp : ((JMenu) result).getMenuComponents()) {
                            if (comp instanceof JMenu) {
                                if (part.equals(WizDesk.delMnemonic(((JMenu) comp)
                                                .getText()))) {
                                    result = (JMenu) comp;
                                    found = true;
                                }
                            } else if (comp instanceof JMenuItem) {
                                if (part.equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                                .getText()))) {
                                    result = (JMenuItem) comp;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            return null;
                        }
                    } else {
                        var found = false;
                        for (Component comp : result.getComponents()) {
                            if (comp instanceof JMenu) {
                                if (part.equals(WizDesk.delMnemonic(((JMenu) comp)
                                                .getText()))) {
                                    result = (JMenu) comp;
                                    found = true;
                                }
                            } else if (comp instanceof JMenuItem) {
                                if (part.equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                                .getText()))) {
                                    result = (JMenuItem) comp;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            return null;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static JMenu getMenu(JMenuBar fromBar, String onPath) {
        JMenu result = null;
        if (onPath != null) {
            var parts = onPath.split("\\.");
            if (parts.length > 0) {
                for (Component comp : fromBar.getComponents()) {
                    if (comp instanceof JMenu) {
                        if (parts[0].equals(WizDesk.delMnemonic(((JMenu) comp)
                                        .getText()))) {
                            result = (JMenu) comp;
                            break;
                        }
                    }
                }
                for (var ip = 1; ip < parts.length; ip++) {
                    result = WizDesk.getMenu(result, parts[ip]);
                    if (result == null) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static JMenu getMenu(JPopupMenu doPopup, String onPath) {
        JMenu result = null;
        if (onPath != null) {
            var parts = onPath.split("\\.");
            if (parts.length > 0) {
                for (Component comp : doPopup.getComponents()) {
                    if (comp instanceof JMenu) {
                        if (parts[0].equals(WizDesk.delMnemonic(((JMenu) comp)
                                        .getText()))) {
                            result = (JMenu) comp;
                            break;
                        }
                    }
                }
                for (var ip = 1; ip < parts.length; ip++) {
                    result = WizDesk.getMenu(result, parts[ip]);
                    if (result == null) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static JMenu getMenu(JMenu fromMenu, String withTitle) {
        JMenu result = null;
        if (withTitle != null) {
            for (Component comp : fromMenu.getMenuComponents()) {
                if (comp instanceof JMenu) {
                    if (withTitle.equals(WizDesk.delMnemonic(((JMenu) comp).getText()))) {
                        result = (JMenu) comp;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static JMenuItem getMenuItem(JPopupMenu doPopup, String onPath) {
        if (onPath != null) {
            var parts = onPath.split("\\.");
            if (parts.length > 0) {
                JMenu menu = null;
                for (Component comp : doPopup.getComponents()) {
                    if (comp instanceof JMenu) {
                        if (parts[0].equals(WizDesk.delMnemonic(((JMenu) comp)
                                        .getText()))) {
                            menu = (JMenu) comp;
                            break;
                        }
                    } else if (comp instanceof JMenuItem) {
                        if (parts[0].equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                        .getText()))) {
                            return (JMenuItem) comp;
                        }
                    }
                }
                if (menu == null) {
                    return null;
                }
                for (var ip = 1; ip < parts.length - 1; ip++) {
                    menu = WizDesk.getMenu(menu, parts[ip]);
                    if (menu == null) {
                        return null;
                    }
                }
                return WizDesk.getMenuItem(menu, parts[parts.length - 1]);
            }
        }
        return null;
    }

    public static JMenuItem getMenuItem(JMenu fromMenu, String withTitle) {
        JMenuItem result = null;
        if (withTitle != null) {
            for (Component comp : fromMenu.getMenuComponents()) {
                if (comp instanceof JMenuItem) {
                    if (withTitle.equals(WizDesk.delMnemonic(((JMenuItem) comp)
                                    .getText()))) {
                        result = (JMenuItem) comp;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static void execute(ActionListener[] actions) {
        if (actions != null) {
            for (ActionListener act : actions) {
                act.actionPerformed(null);
            }
        }
    }

    public static void callOrInvoke(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public static void callOrWait(Runnable runnable) throws Exception {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeAndWait(runnable);
        }
    }

}
