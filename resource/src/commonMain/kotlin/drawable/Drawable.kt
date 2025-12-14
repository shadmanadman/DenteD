package drawable

import jaw_generation.resource.generated.resources.Res
import jaw_generation.resource.generated.resources.ic_close
import jaw_generation.resource.generated.resources.ic_detected_teeth_1
import jaw_generation.resource.generated.resources.ic_detected_teeth_2
import jaw_generation.resource.generated.resources.ic_detected_teeth_3
import jaw_generation.resource.generated.resources.ic_detected_teeth_4
import jaw_generation.resource.generated.resources.ic_detected_teeth_5
import jaw_generation.resource.generated.resources.ic_detected_teeth_6
import jaw_generation.resource.generated.resources.ic_detected_teeth_7
import jaw_generation.resource.generated.resources.ic_detected_teeth_8
import jaw_generation.resource.generated.resources.ic_front_jaw
import jaw_generation.resource.generated.resources.ic_half_rectangle
import jaw_generation.resource.generated.resources.ic_info_circle
import jaw_generation.resource.generated.resources.ic_lighting
import jaw_generation.resource.generated.resources.ic_lower_jaw
import jaw_generation.resource.generated.resources.ic_missed_teeth_1
import jaw_generation.resource.generated.resources.ic_missed_teeth_2
import jaw_generation.resource.generated.resources.ic_missed_teeth_3
import jaw_generation.resource.generated.resources.ic_missed_teeth_4
import jaw_generation.resource.generated.resources.ic_missed_teeth_5
import jaw_generation.resource.generated.resources.ic_missed_teeth_6
import jaw_generation.resource.generated.resources.ic_missed_teeth_7
import jaw_generation.resource.generated.resources.ic_missed_teeth_8
import jaw_generation.resource.generated.resources.ic_teeth_1
import jaw_generation.resource.generated.resources.ic_teeth_2
import jaw_generation.resource.generated.resources.ic_teeth_3
import jaw_generation.resource.generated.resources.ic_teeth_4
import jaw_generation.resource.generated.resources.ic_teeth_5
import jaw_generation.resource.generated.resources.ic_teeth_6
import jaw_generation.resource.generated.resources.ic_teeth_7
import jaw_generation.resource.generated.resources.ic_teeth_8
import jaw_generation.resource.generated.resources.ic_upper

object Drawable {
    val ic_half_rectangle = Res.drawable.ic_half_rectangle
    val close = Res.drawable.ic_close
    val lightening = Res.drawable.ic_lighting
    val info = Res.drawable.ic_info_circle
    val upper_jaw = Res.drawable.ic_upper
    val front_jaw = Res.drawable.ic_front_jaw
    val lower_jaw = Res.drawable.ic_lower_jaw

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