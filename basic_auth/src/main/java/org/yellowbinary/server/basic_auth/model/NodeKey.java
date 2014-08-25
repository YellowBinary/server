package org.yellowbinary.server.basic_auth.model;

import org.springframework.security.acls.domain.IdentityUnavailableException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class NodeKey {

    //~ Instance fields ================================================================================================

    private final String type;
    private String identifier;

    //~ Constructors ===================================================================================================

    public NodeKey(String type, String identifier) {
        Assert.hasText(type, "Type required");
        Assert.notNull(identifier, "identifier required");

        this.identifier = identifier;
        this.type = type;
    }

    /**
     * Constructor which uses the name of the supplied class as the <tt>type</tt> property.
     */
    public NodeKey(Class<?> javaType, String identifier) {
        Assert.notNull(javaType, "Java Type required");
        Assert.notNull(identifier, "identifier required");
        this.type = javaType.getName();
        this.identifier = identifier;
    }

    /**
     * Creates the <code>ObjectIdentityImpl</code> based on the passed
     * object instance. The passed object must provide a <code>getId()</code>
     * method, otherwise an exception will be thrown.
     * <p>
     * The class name of the object passed will be considered the {@link #type}, so if more control is required,
     * a different constructor should be used.
     *
     * @param object the domain object instance to create an identity for.
     *
     * @throws org.springframework.security.acls.domain.IdentityUnavailableException if identity could not be extracted
     */
    public NodeKey(Object object) throws IdentityUnavailableException {
        Assert.notNull(object, "object cannot be null");

        Class<?> typeClass = ClassUtils.getUserClass(object.getClass());
        type = typeClass.getName();

        Object result;

        try {
            Method method = typeClass.getMethod("getKey", new Class[] {});
            result = method.invoke(object);
        } catch (Exception e) {
            throw new IdentityUnavailableException("Could not extract identity from object " + object, e);
        }

        Assert.notNull(result, "getKey() is required to return a non-null value");
        Assert.isInstanceOf(String.class, result, "Getter must provide a return value of type String");
        this.identifier = (String) result;
    }

    //~ Methods ========================================================================================================

    /**
     * Important so caching operates properly.
     * <p>
     * Considers an object of the same class equal if it has the same <code>classname</code> and
     * <code>id</code> properties.
     * <p>
     * Numeric identities (Integer and Long values) are considered equal if they are numerically equal. Other
     * serializable types are evaluated using a simple equality.
     *
     * @param arg0 object to compare
     *
     * @return <code>true</code> if the presented object matches this object
     */
    public boolean equals(Object arg0) {
        if (arg0 == null || !(arg0 instanceof NodeKey)) {
            return false;
        }

        NodeKey other = (NodeKey) arg0;

        return identifier.equals(other.identifier) && type.equals(other.type);

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    /**
     * Important so caching operates properly.
     *
     * @return the hash
     */
    public int hashCode() {
        int code = 31;
        code ^= this.type.hashCode();
        code ^= this.identifier.hashCode();

        return code;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append("[");
        sb.append("Type: ").append(this.type);
        sb.append("; Identifier: ").append(this.identifier).append("]");

        return sb.toString();
    }
}
