/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extension;

import host.IHostApplication;

/**
 *
 * @author duongtb
 */
public interface IExtensionFactory {
    IExtension createExtension(IHostApplication inHostApp);
}
