/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extension;

/**
 *
 * @author duongtb
 */
public interface IExtension {
    String getExtensionName();
    String getExtensionTitle();
    void executeVisualization();
    String getCreditInformation();
}
