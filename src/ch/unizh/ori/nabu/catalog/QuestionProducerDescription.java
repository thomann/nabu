/*
 * QuestionProducerDescription.java
 *
 * Created on 27. Dezember 2002, 13:23
 */

package ch.unizh.ori.nabu.catalog;

import java.util.List;

import ch.unizh.ori.nabu.core.QuestionProducer;

/**
 *
 * @author  pht
 */
public interface QuestionProducerDescription {
    
    public String getName();

    public String getKey();
    
    public String getDescription();
    
    public QuestionProducer createProducer();
    
    public List getSubQuestionProducerDescriptions();
    
}
