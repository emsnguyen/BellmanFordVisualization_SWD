/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import extension.IExtension;
import extension.IExtensionFactory;
import extension.MyExtensionFrame;
import host.IHostApplication;

/**
 *
 * @author duongtb
 */
public class ExtensionFactory implements IExtensionFactory {
    public IExtension createExtension(IHostApplication inHostApp)
    {
        MyExtensionFrame ext = new MyExtensionFrame();
        ext.setHostApp(inHostApp);
        return ext;
    }
}
