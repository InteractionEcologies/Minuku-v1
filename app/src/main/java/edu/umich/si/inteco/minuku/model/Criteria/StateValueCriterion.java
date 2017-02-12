package edu.umich.si.inteco.minuku.model.Criteria;

/**
 * Created by Armuro on 10/28/15.
 */
public class StateValueCriterion extends Criterion {

    public StateValueCriterion(){
        super();
    }

//    public StateValueCriterion(String measure, String relationship, float targetValue){
//        super();
//        mMeasure = measure;
//        mRelationship =relationship;
//        mTargetFloatValue = targetValue;
//        isTargetString = false;
//    }
//
//    public StateValueCriterion(String measure, String relationship, String targetValue){
//        super();
//        mMeasure = measure;
//        mRelationship =relationship;
//        mTargetStringValue = targetValue;
//        isTargetString = true;
//    }
//
//    public boolean isTargetString() {
//        return isTargetString;
//    }
//
//    public void setIsTargetString(boolean isTargetString) {
//        this.isTargetString = isTargetString;
//    }

    public void setRelationship(String relationship){
        mRelationship = relationship;
    }

    public String getRelationship(){
        return mRelationship;
    }

//    public void setTargetValue(float value){
//        mTargetFloatValue = value;
//    }
//
//    public void setTargetValue(String value){
//        mTargetStringValue = value;
//    }
//
//    public String getTargetStringValue() {
//        return mTargetStringValue;
//    }
//
//    public float getTargetFloatValue() {
//        return mTargetFloatValue;
//    }

//
//    @Override
//    public String toString() {
//
//        return "Criterion{" +
//                mMeasure + "-" +
//                mRelationship + "-" +
//                mTargetValue+
//                '}';
//    }
}
