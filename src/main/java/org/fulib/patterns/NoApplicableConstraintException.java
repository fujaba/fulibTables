package org.fulib.patterns;

/**
 * @since 1.2
 */
public class NoApplicableConstraintException extends RuntimeException
{
   public NoApplicableConstraintException()
   {
   }

   public NoApplicableConstraintException(String message)
   {
      super(message);
   }

   public NoApplicableConstraintException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public NoApplicableConstraintException(Throwable cause)
   {
      super(cause);
   }
}
