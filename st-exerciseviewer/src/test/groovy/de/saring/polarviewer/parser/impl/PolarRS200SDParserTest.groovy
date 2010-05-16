package de.saring.polarviewer.parser.impl

import de.saring.polarviewer.core.PVException
import de.saring.polarviewer.data.PVExercise
import de.saring.polarviewer.parser.ExerciseParser

/**
 * This class contains all unit tests for the PolarRS200SDParser class.
 *
 * @author Stefan Saring
 */
class PolarRS200SDParserTest extends GroovyTestCase {
    
    /** Instance to be tested. */
    private ExerciseParser parser
    
    /**
     * This method initializes the environment for testing.
     */
    void setUp () {
        parser = new PolarRS200SDParser ()
    }
    
    /**
     * This method must fail on parsing an exerise file which doesn't exists.
     */
    void testParseExerciseMissingFile () {
        shouldFail (PVException) {
            parser.parseExercise ('misc/testdata/rs200sd-sample-123.xml')
        }
    }
    
    /**
     * This test method parses an RS200SD exercise file with speed data.
     */
    void testParseExerciseWithSpeedData () {
        
        // parse exercise file
        def exercise = parser.parseExercise ('misc/testdata/rs200sd-sample.xml')
        
        // check exercise data
        assertEquals (PVExercise.ExerciseFileType.RS200SDRAW, exercise.fileType)
        
        def calDate = Calendar.getInstance ()
        calDate.set (2006, 2-1, 20, 16, 54, 11)
        assertEquals ((int) (calDate.time.time / 1000), (int) (exercise.date.time / 1000))
        assertEquals (28 * 60 * 10 + 51 * 10 + 5, exercise.duration)            
        
        // heart rates
        assertEquals (161, exercise.heartRateAVG)
        assertEquals (181, exercise.heartRateMax)
        
        // energy
        assertEquals (404, exercise.energy)
        
        // heartrate limits
        assertEquals (5, exercise.heartRateLimits.size ())
        assertEquals (100, exercise.heartRateLimits[0].lowerHeartRate)
        assertEquals (118, exercise.heartRateLimits[0].upperHeartRate)
        assertEquals (25, exercise.heartRateLimits[0].timeWithin)
        assertEquals (140, exercise.heartRateLimits[2].lowerHeartRate)
        assertEquals (158, exercise.heartRateLimits[2].upperHeartRate)
        assertEquals (485, exercise.heartRateLimits[2].timeWithin)
        
        // distance & speed & odometer
        assertEquals (5680, exercise.speed.distance)
        assertEquals (13745, (int) (exercise.speed.speedMax * 1000))
        assertEquals (11811, (int) (exercise.speed.speedAVG * 1000))
        
        // lap data
        assertEquals  (6, exercise.lapList.size ())
        
        assertEquals (173, exercise.lapList[0].heartRateSplit)        
        assertEquals (151, exercise.lapList[0].heartRateAVG)
        assertEquals (173, exercise.lapList[0].heartRateMax)
        assertEquals (1000, exercise.lapList[0].speed.distance)
        assertEquals (11428, (int) (exercise.lapList[0].speed.speedAVG * 1000))
        assertEquals (12756, (int) (exercise.lapList[0].speed.speedEnd * 1000))
        
        assertEquals (165, exercise.lapList[2].heartRateSplit)        
        assertEquals (165, exercise.lapList[2].heartRateAVG)
        assertEquals (174, exercise.lapList[2].heartRateMax)
        assertEquals (3000, exercise.lapList[2].speed.distance)
        assertEquals (11842, (int) (exercise.lapList[2].speed.speedAVG * 1000))
        assertEquals (13249, (int) (exercise.lapList[2].speed.speedEnd * 1000))
        
        assertEquals (181, exercise.lapList[5].heartRateSplit)        
        assertEquals (172, exercise.lapList[5].heartRateAVG)
        assertEquals (181, exercise.lapList[5].heartRateMax)
        assertEquals (5680, exercise.lapList[5].speed.distance)
        assertEquals (11780, (int) (exercise.lapList[5].speed.speedAVG * 1000))
        assertEquals (12829, (int) (exercise.lapList[5].speed.speedEnd * 1000))
    }
}
