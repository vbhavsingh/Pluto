package com.analyzer;

/**
 * helper class to check the operating system this Java VM runs in
 *
 * please keep the notes below as a pseudo-license
 *
 */
import com.analyzer.commons.LocalConstants;
import java.util.Locale;
public  final class OsCheck {
  /**
   * types of Operating Systems
   */

  // cached result of OS detection
  protected static LocalConstants.OSType detectedOS;

  /**
   * detect the operating system from the os.name System property and cache
   * the result
   * 
   * @returns - the operating system detected
   */
  public static LocalConstants.OSType getOperatingSystemType() {
    if (detectedOS == null) {
      String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
      if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
        detectedOS = LocalConstants.OSType.MacOS;
      } else if (OS.indexOf("win") >= 0) {
        detectedOS = LocalConstants.OSType.Windows;
      } else if (OS.indexOf("nux") >= 0) {
        detectedOS = LocalConstants.OSType.Linux;
      } else {
        detectedOS = LocalConstants.OSType.Other;
      }
    }
    return detectedOS;
  }
}