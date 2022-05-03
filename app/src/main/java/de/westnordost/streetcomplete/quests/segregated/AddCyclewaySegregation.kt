package de.westnordost.streetcomplete.quests.segregated

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.osmquests.Tags
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.BICYCLIST
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.OUTDOORS
import de.westnordost.streetcomplete.osm.ANYTHING_PAVED
import de.westnordost.streetcomplete.osm.updateWithCheckDate
import de.westnordost.streetcomplete.util.ktx.toYesNo

class AddCyclewaySegregation : OsmFilterQuestType<Boolean>() {

    override val elementFilter = """
        ways with
        (
          (highway = path and bicycle = designated and foot = designated)
          or (highway = footway and bicycle = designated)
          or (highway = cycleway and foot ~ designated|yes)
        )
        and surface ~ ${ANYTHING_PAVED.joinToString("|")}
        and area != yes
        and !sidewalk
        and (!segregated or segregated older today -8 years)
    """
    override val changesetComment = "Add segregated status for combined footway with cycleway"
    override val wikiLink = "Key:segregated"
    override val icon = R.drawable.ic_quest_path_segregation
    override val achievements = listOf(BICYCLIST, OUTDOORS)
    override val isSplitWayEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_segregated_title

    override fun createForm() = AddCyclewaySegregationForm()

    override fun applyAnswerTo(answer: Boolean, tags: Tags, timestampEdited: Long) {
        tags.updateWithCheckDate("segregated", answer.toYesNo())
    }
}
