package resource

import shared.resources.Res
import shared.resources.ic_close
import shared.resources.ic_detected_teeth_1
import shared.resources.ic_detected_teeth_2
import shared.resources.ic_detected_teeth_3
import shared.resources.ic_detected_teeth_4
import shared.resources.ic_detected_teeth_5
import shared.resources.ic_detected_teeth_6
import shared.resources.ic_detected_teeth_7
import shared.resources.ic_detected_teeth_8
import shared.resources.ic_front_jaw
import shared.resources.ic_half_rectangle
import shared.resources.ic_info_circle
import shared.resources.ic_lighting
import shared.resources.ic_lower_jaw
import shared.resources.ic_missed_teeth_1
import shared.resources.ic_missed_teeth_2
import shared.resources.ic_missed_teeth_3
import shared.resources.ic_missed_teeth_4
import shared.resources.ic_missed_teeth_5
import shared.resources.ic_missed_teeth_6
import shared.resources.ic_missed_teeth_7
import shared.resources.ic_missed_teeth_8
import shared.resources.ic_teeth_01
import shared.resources.ic_teeth_02
import shared.resources.ic_teeth_03
import shared.resources.ic_teeth_04
import shared.resources.ic_teeth_05
import shared.resources.ic_teeth_06
import shared.resources.ic_teeth_07
import shared.resources.ic_teeth_08
import shared.resources.ic_teeth_1
import shared.resources.ic_teeth_2
import shared.resources.ic_teeth_3
import shared.resources.ic_teeth_4
import shared.resources.ic_teeth_5
import shared.resources.ic_teeth_6
import shared.resources.ic_teeth_7
import shared.resources.ic_teeth_8
import shared.resources.ic_upper
import shared.resources.ic_upper_jaw

object DrawableRes {
    val ic_half_rectangle = Res.drawable.ic_half_rectangle
    val close = Res.drawable.ic_close
    val lightening = Res.drawable.ic_lighting
    val info = Res.drawable.ic_info_circle
    val upper_jaw = Res.drawable.ic_upper
    val front_jaw = Res.drawable.ic_front_jaw
    val lower_jaw = Res.drawable.ic_lower_jaw

    val tooth_illustration_1 = Res.drawable.ic_teeth_01
    val tooth_illustration_2 = Res.drawable.ic_teeth_02
    val tooth_illustration_3 = Res.drawable.ic_teeth_03
    val tooth_illustration_4 = Res.drawable.ic_teeth_04
    val tooth_illustration_5 = Res.drawable.ic_teeth_05
    val tooth_illustration_6 = Res.drawable.ic_teeth_06
    val tooth_illustration_7 = Res.drawable.ic_teeth_07
    val tooth_illustration_8 = Res.drawable.ic_teeth_08


    val upper_jaw_illustration = Res.drawable.ic_upper_jaw
    val lower_jaw_illustration = Res.drawable.ic_lower_jaw

    val not_detected_teeth_stage = listOf(
        Res.drawable.ic_teeth_1,
        Res.drawable.ic_teeth_2,
        Res.drawable.ic_teeth_3,
        Res.drawable.ic_teeth_4,
        Res.drawable.ic_teeth_5,
        Res.drawable.ic_teeth_6,
        Res.drawable.ic_teeth_7,
        Res.drawable.ic_teeth_8,
        Res.drawable.ic_teeth_4,
        Res.drawable.ic_teeth_5,
        Res.drawable.ic_teeth_6,
        Res.drawable.ic_teeth_7,
        Res.drawable.ic_teeth_8,
        Res.drawable.ic_teeth_1,
        Res.drawable.ic_teeth_2,
        Res.drawable.ic_teeth_3,
    )
    val detected_teeth_stage = listOf(
            Res.drawable.ic_detected_teeth_1,
            Res.drawable.ic_detected_teeth_2,
            Res.drawable.ic_detected_teeth_3,
            Res.drawable.ic_detected_teeth_4,
            Res.drawable.ic_detected_teeth_5,
            Res.drawable.ic_detected_teeth_6,
            Res.drawable.ic_detected_teeth_7,
            Res.drawable.ic_detected_teeth_8,
            Res.drawable.ic_detected_teeth_4,
            Res.drawable.ic_detected_teeth_5,
            Res.drawable.ic_detected_teeth_6,
            Res.drawable.ic_detected_teeth_7,
            Res.drawable.ic_detected_teeth_8,
            Res.drawable.ic_detected_teeth_1,
            Res.drawable.ic_detected_teeth_2,
            Res.drawable.ic_detected_teeth_3,
        )

    private val missing_teeth_stage = listOf(
        Res.drawable.ic_missed_teeth_1,
        Res.drawable.ic_missed_teeth_2,
        Res.drawable.ic_missed_teeth_3,
        Res.drawable.ic_missed_teeth_4,
        Res.drawable.ic_missed_teeth_5,
        Res.drawable.ic_missed_teeth_6,
        Res.drawable.ic_missed_teeth_7,
        Res.drawable.ic_missed_teeth_8,
        Res.drawable.ic_missed_teeth_4,
        Res.drawable.ic_missed_teeth_5,
        Res.drawable.ic_missed_teeth_6,
        Res.drawable.ic_missed_teeth_7,
        Res.drawable.ic_missed_teeth_8,
        Res.drawable.ic_missed_teeth_1,
        Res.drawable.ic_missed_teeth_2,
        Res.drawable.ic_missed_teeth_3,
    )
}