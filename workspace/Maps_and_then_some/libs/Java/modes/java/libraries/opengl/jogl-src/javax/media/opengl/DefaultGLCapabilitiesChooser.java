/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package javax.media.opengl;

import com.sun.opengl.impl.Debug;

/** <P> The default implementation of the {@link
    GLCapabilitiesChooser} interface, which provides consistent visual
    selection behavior across platforms. The precise algorithm is
    deliberately left loosely specified. Some properties are: </P>

    <UL>

    <LI> As long as there is at least one available non-null
    GLCapabilities which matches the "stereo" option, will return a
    valid index.

    <LI> Attempts to match as closely as possible the given
    GLCapabilities, but will select one with fewer capabilities (i.e.,
    lower color depth) if necessary.

    <LI> Prefers hardware-accelerated visuals to
    non-hardware-accelerated.

    <LI> If there is no exact match, prefers a more-capable visual to
    a less-capable one.

    <LI> If there is more than one exact match, chooses an arbitrary
    one.

    <LI> May select the opposite of a double- or single-buffered
    visual (based on the user's request) in dire situations.

    <LI> Color depth (including alpha) mismatches are weighted higher
    than depth buffer mismatches, which are in turn weighted higher
    than accumulation buffer (including alpha) and stencil buffer
    depth mismatches.

    <LI> If a valid windowSystemRecommendedChoice parameter is
    supplied, chooses that instead of using the cross-platform code.

    </UL>
*/

public class DefaultGLCapabilitiesChooser implements GLCapabilitiesChooser {
  private static final boolean DEBUG = Debug.debug("DefaultGLCapabilitiesChooser");

  public int chooseCapabilities(GLCapabilities desired,
                                GLCapabilities[] available,
                                int windowSystemRecommendedChoice) {
    if (DEBUG) {
      System.err.println("Desired: " + desired);
      for (int i = 0; i < available.length; i++) {
        System.err.println("Available " + i + ": " + available[i]);
      }
      System.err.println("Window system's recommended choice: " + windowSystemRecommendedChoice);
    }

    if (windowSystemRecommendedChoice >= 0 &&
        windowSystemRecommendedChoice < available.length &&
        available[windowSystemRecommendedChoice] != null) {
      if (DEBUG) {
        System.err.println("Choosing window system's recommended choice of " + windowSystemRecommendedChoice);
        System.err.println(available[windowSystemRecommendedChoice]);
      }
      return windowSystemRecommendedChoice;
    }

    // Create score array
    int[] scores = new int[available.length];
    int NO_SCORE = -9999999;
    int DOUBLE_BUFFER_MISMATCH_PENALTY = 1000;
    int STENCIL_MISMATCH_PENALTY = 500;
    // Pseudo attempt to keep equal rank penalties scale-equivalent
    // (e.g., stencil mismatch is 3 * accum because there are 3 accum
    // components)
    int COLOR_MISMATCH_PENALTY_SCALE     = 36;
    int DEPTH_MISMATCH_PENALTY_SCALE     = 6;
    int ACCUM_MISMATCH_PENALTY_SCALE     = 1;
    int STENCIL_MISMATCH_PENALTY_SCALE   = 3;
    for (int i = 0; i < scores.length; i++) {
      scores[i] = NO_SCORE;
    }
    // Compute score for each
    for (int i = 0; i < scores.length; i++) {
      GLCapabilities cur = available[i];
      if (cur == null) {
        continue;
      }
      if (desired.getStereo() != cur.getStereo()) {
        continue;
      }
      int score = 0;
      // Compute difference in color depth
      // (Note that this decides the direction of all other penalties)
      score += (COLOR_MISMATCH_PENALTY_SCALE *
                ((cur.getRedBits() + cur.getGreenBits() + cur.getBlueBits() + cur.getAlphaBits()) -
                 (desired.getRedBits() + desired.getGreenBits() + desired.getBlueBits() + desired.getAlphaBits())));
      // Compute difference in depth buffer depth
      score += (DEPTH_MISMATCH_PENALTY_SCALE * sign(score) * 
                Math.abs(cur.getDepthBits() - desired.getDepthBits()));
      // Compute difference in accumulation buffer depth
      score += (ACCUM_MISMATCH_PENALTY_SCALE * sign(score) *
                Math.abs((cur.getAccumRedBits() + cur.getAccumGreenBits() + cur.getAccumBlueBits() + cur.getAccumAlphaBits()) -
                         (desired.getAccumRedBits() + desired.getAccumGreenBits() + desired.getAccumBlueBits() + desired.getAccumAlphaBits())));
      // Compute difference in stencil bits
      score += STENCIL_MISMATCH_PENALTY_SCALE * sign(score) * (cur.getStencilBits() - desired.getStencilBits());
      if (cur.getDoubleBuffered() != desired.getDoubleBuffered()) {
        score += sign(score) * DOUBLE_BUFFER_MISMATCH_PENALTY;
      }
      if ((desired.getStencilBits() > 0) && (cur.getStencilBits() == 0)) {
        score += sign(score) * STENCIL_MISMATCH_PENALTY;
      }
      scores[i] = score;
    }
    // Now prefer hardware-accelerated visuals by pushing scores of
    // non-hardware-accelerated visuals out
    boolean gotHW = false;
    int maxAbsoluteHWScore = 0;
    for (int i = 0; i < scores.length; i++) {
      int score = scores[i];
      if (score == NO_SCORE) {
        continue;
      }
      GLCapabilities cur = available[i];
      if (cur.getHardwareAccelerated()) {
        int absScore = Math.abs(score);
        if (!gotHW ||
            (absScore > maxAbsoluteHWScore)) {
          gotHW = true;
          maxAbsoluteHWScore = absScore;
        }
      }
    }
    if (gotHW) {
      for (int i = 0; i < scores.length; i++) {
        int score = scores[i];
        if (score == NO_SCORE) {
          continue;
        }
        GLCapabilities cur = available[i];
        if (!cur.getHardwareAccelerated()) {
          if (score <= 0) {
            score -= maxAbsoluteHWScore;
          } else if (score > 0) {
            score += maxAbsoluteHWScore;
          }
          scores[i] = score;
        }
      }
    }

    if (DEBUG) {
      System.err.print("Scores: [");
      for (int i = 0; i < available.length; i++) {
        if (i > 0) {
          System.err.print(",");
        }
        System.err.print(" " + scores[i]);
      }
      System.err.println(" ]");
    }

    // Ready to select. Choose score closest to 0. 
    int scoreClosestToZero = NO_SCORE;
    int chosenIndex = -1;
    for (int i = 0; i < scores.length; i++) {
      int score = scores[i];
      if (score == NO_SCORE) {
        continue;
      }
      // Don't substitute a positive score for a smaller negative score
      if ((scoreClosestToZero == NO_SCORE) ||
          (Math.abs(score) < Math.abs(scoreClosestToZero) &&
	   ((sign(scoreClosestToZero) < 0) || (sign(score) > 0)))) {
        scoreClosestToZero = score;
        chosenIndex = i;
      }
    }
    if (chosenIndex < 0) {
      throw new GLException("Unable to select one of the provided GLCapabilities");
    }
    if (DEBUG) {
      System.err.println("Chosen index: " + chosenIndex);
      System.err.println("Chosen capabilities:");
      System.err.println(available[chosenIndex]);
    }

    return chosenIndex;
  }

  private static int sign(int score) {
    if (score < 0) {
      return -1;
    }
    return 1;
  }
}
