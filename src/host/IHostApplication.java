package host;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import extension.IExtension;
import java.util.ArrayList;

/**
 *
 * @author duongtb
 */
public interface IHostApplication {
    void registerExtension(IExtension ext);
    void unregisterExtension(IExtension ext);
    ArrayList<IExtension> getExtensionList();
}
