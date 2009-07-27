
package ch.unizh.ori.nabu.ui.swing.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.unizh.ori.nabu.ui.swing.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetVocsResponse_QNAME = new QName("http://webservices.http.ui.nabu.ori.unizh.ch/", "getVocsResponse");
    private final static QName _GetSubsResponse_QNAME = new QName("http://webservices.http.ui.nabu.ori.unizh.ch/", "getSubsResponse");
    private final static QName _GetVocs_QNAME = new QName("http://webservices.http.ui.nabu.ori.unizh.ch/", "getVocs");
    private final static QName _GetSubs_QNAME = new QName("http://webservices.http.ui.nabu.ori.unizh.ch/", "getSubs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.unizh.ori.nabu.ui.swing.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSubs }
     * 
     */
    public GetSubs createGetSubs() {
        return new GetSubs();
    }

    /**
     * Create an instance of {@link GetVocsResponse }
     * 
     */
    public GetVocsResponse createGetVocsResponse() {
        return new GetVocsResponse();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link GetSubsResponse }
     * 
     */
    public GetSubsResponse createGetSubsResponse() {
        return new GetSubsResponse();
    }

    /**
     * Create an instance of {@link GetVocs }
     * 
     */
    public GetVocs createGetVocs() {
        return new GetVocs();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVocsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.http.ui.nabu.ori.unizh.ch/", name = "getVocsResponse")
    public JAXBElement<GetVocsResponse> createGetVocsResponse(GetVocsResponse value) {
        return new JAXBElement<GetVocsResponse>(_GetVocsResponse_QNAME, GetVocsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.http.ui.nabu.ori.unizh.ch/", name = "getSubsResponse")
    public JAXBElement<GetSubsResponse> createGetSubsResponse(GetSubsResponse value) {
        return new JAXBElement<GetSubsResponse>(_GetSubsResponse_QNAME, GetSubsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVocs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.http.ui.nabu.ori.unizh.ch/", name = "getVocs")
    public JAXBElement<GetVocs> createGetVocs(GetVocs value) {
        return new JAXBElement<GetVocs>(_GetVocs_QNAME, GetVocs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.http.ui.nabu.ori.unizh.ch/", name = "getSubs")
    public JAXBElement<GetSubs> createGetSubs(GetSubs value) {
        return new JAXBElement<GetSubs>(_GetSubs_QNAME, GetSubs.class, null, value);
    }

}
