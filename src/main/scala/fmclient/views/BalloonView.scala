package fmclient.views

import java.awt.Color
import net.java.balloontip.CustomFlexBalloonTip
import net.java.balloontip.FlexBalloonTip.{ Orientation, AttachLocation }
import net.java.balloontip.styles.RoundedBalloonStyle
import javax.swing.JComponent

class BalloonView(val attach : JComponent) extends CustomFlexBalloonTip(attach, 
                                                    new RoundedBalloonStyle(5, 5, Color.LIGHT_GRAY, Color.BLACK),
                                                    Orientation.LEFT_BELOW,
                                                    AttachLocation.ALIGNED, 20, 15)
