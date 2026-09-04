package edu.berkeley.cs186.database.concurrency;

import edu.berkeley.cs186.database.TransactionContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LockUtil is a declarative layer which simplifies multigranularity lock
 * acquisition for the user (you, in the last task of Part 2). Generally
 * speaking, you should use LockUtil for lock acquisition instead of calling
 * LockContext methods directly.
 */
public class LockUtil {
    /**
     * Ensure that the current transaction can perform actions requiring
     * `requestType` on `lockContext`.
     *
     * `requestType` is guaranteed to be one of: S, X, NL.
     *
     * This method should promote/escalate/acquire as needed, but should only
     * grant the least permissive set of locks needed. We recommend that you
     * think about what to do in each of the following cases:
     * - The current lock type can effectively substitute the requested type
     * - The current lock type is IX and the requested lock is S
     * - The current lock type is an intent lock
     * - None of the above: In this case, consider what values the explicit
     *   lock type can be, and think about how ancestor looks will need to be
     *   acquired or changed.
     *
     * You may find it useful to create a helper method that ensures you have
     * the appropriate locks on all ancestors.
     */
    public static void ensureSufficientLockHeld(LockContext lockContext, LockType requestType) {
        // requestType must be S, X, or NL
        assert (requestType == LockType.S || requestType == LockType.X || requestType == LockType.NL);

        // Do nothing if the transaction or lockContext is null
        TransactionContext transaction = TransactionContext.getTransaction();
        if (transaction == null || lockContext == null) return;

        // You may find these variables useful
        LockContext parentContext = lockContext.parentContext();
        LockType effectiveLockType = lockContext.getEffectiveLockType(transaction);
        LockType explicitLockType = lockContext.getExplicitLockType(transaction);

        // TODO(proj4_part2): implement
        if (requestType.equals(LockType.NL)) {
            return;
        }
        if (LockType.substitutable(effectiveLockType, requestType)) {
            return;
        }
        if (requestType.equals(LockType.S)) {
            handleRequestS(lockContext, parentContext, explicitLockType,
                    transaction);
        } else { // X requests
            handleRequestX(lockContext, parentContext, explicitLockType,
                    transaction);

        }
        // testing
//         if (explicitLockType.equals(LockType.IX) && requestType.equals(LockType.S)) {
//            lockContext.promote(transaction, LockType.SIX);
//        } else if (explicitLockType.isIntent()) {
//            lockContext.escalate(transaction);
//        } else if (explicitLockType.equals(LockType.NL) && requestType.equals(LockType.S)) {
//            handleNLAndRequestS(lockContext, parentContext, effectiveLockType, transaction);
//        } else if (explicitLockType.equals(LockType.NL) && requestType.equals(LockType.X)) {
//            handleNLAndRequestX(lockContext, parentContext, effectiveLockType, transaction);
//        } else if (explicitLockType.equals(requestType) || effectiveLockType.equals(requestType)) {
//            lockContext.escalate(transaction);
//        } else if (requestType.equals(LockType.S)) {
//            handleRequestS(lockContext, parentContext, effectiveLockType, transaction);
//        } else if (requestType.equals(LockType.X)) {
//            handleRequestX(lockContext, parentContext, effectiveLockType, transaction);
//        } else if (LockType.substitutable(requestType, explicitLockType)) {
//             System.out.println(requestType);
//             System.out.println(explicitLockType);
//             lockContext.promote(transaction, requestType);
//         }
   }

    // TODO(proj4_part2) add any helper methods you want

//
//    public static void handleNLAndRequestS(LockContext lockContext, LockContext parentContext,
//                                            LockType explicitLockType, TransactionContext transaction) {
//        if (explicitLockType.equals(LockType.NL)) {
//            ensureAncestors(parentContext, LockType.IS);
//        } else if (explicitLockType.equals(LockType.S)) {
//            ensureAncestors(parentContext, LockType.S);
//        }
//        lockContext.acquire(transaction, LockType.S);
//    }
//
//    public static void handleNLAndRequestX(LockContext lockContext, LockContext parentContext,
//                                            LockType explicitLockType, TransactionContext transaction) {
//        if (explicitLockType.equals(LockType.NL)) {
//            ensureAncestors(parentContext, LockType.IX);
//        } else if (explicitLockType.equals(LockType.X)) {
//            ensureAncestors(parentContext, LockType.X);
//        }
//        lockContext.acquire(transaction, LockType.X);
//    }
//
    public static void handleRequestS(LockContext lockContext, LockContext parentContext,
                                       LockType explicitLockType, TransactionContext transaction) {
        if (explicitLockType.equals(LockType.NL)) {
            ensureAncestors(parentContext, LockType.S);
            lockContext.acquire(transaction, LockType.S);
        } else if (explicitLockType.equals(LockType.IS)) {
            ensureAncestors(parentContext, LockType.S);
            lockContext.escalate(transaction);
        } else {
            ensureAncestors(parentContext, LockType.S);
            lockContext.promote(transaction, LockType.SIX);
        }
    }

    public static void handleRequestX(LockContext lockContext, LockContext parentContext,
                                       LockType explicitLockType, TransactionContext transaction) {
        if (explicitLockType.equals(LockType.NL)) {
            ensureAncestors(parentContext, LockType.X);
            lockContext.acquire(transaction, LockType.X);
        } else if (explicitLockType.equals(LockType.IS)) {
            ensureAncestors(parentContext, LockType.X);
            lockContext.escalate(transaction); // escalate to non intent then
            // promo
            lockContext.promote(transaction, LockType.X);
        } else if (explicitLockType.equals(LockType.S)) {
            ensureAncestors(parentContext, LockType.X);
            lockContext.promote(transaction, LockType.X);
        }
        else {
            ensureAncestors(parentContext, LockType.X);
            lockContext.escalate(transaction);
        }
    }

    public static void ensureAncestors(LockContext lc, LockType lockCase) {
        TransactionContext txn = TransactionContext.getTransaction();
        List<LockContext> ancestry = allAncestors(lc);
        for (LockContext ancestor : ancestry) {
            LockType expLock = ancestor.getExplicitLockType(txn);
            if (lockCase.equals(LockType.S)) {
                if (expLock.equals(LockType.NL)) {
                    ancestor.acquire(txn, LockType.IS);
                }
//            } else if (lockCase.equals(LockType.IX)) {
//                if (expLock.equals(LockType.NL)) {
//                    ancestor.acquire(txn, LockType.IX);
//                } else if (expLock.equals(LockType.IS)) {
//                    ancestor.promote(txn, LockType.IX);
//                }
//            } else if (lockCase.equals(LockType.IS)) {
//                if (expLock.equals(LockType.NL)) {
//                    ancestor.acquire(txn, LockType.IS);
//                }
            } else if (lockCase.equals(LockType.X)) {
                if (expLock.equals(LockType.NL)) {
                    ancestor.acquire(txn, LockType.IX);
                } else if (expLock.equals(LockType.S)) {
                    ancestor.promote(txn, LockType.SIX);
                } else if (expLock.equals(LockType.IS)) {
                    ancestor.promote(txn, LockType.IX);
                }
            }
        }
    }

    public static List<LockContext> allAncestors(LockContext parent) {
        ArrayList<LockContext> ancestry = new ArrayList<>();
        LockContext ancestor = parent;
        while (ancestor != null) {
            ancestry.add(ancestor);
            ancestor = ancestor.parentContext();
        }
        Collections.reverse(ancestry);
        return ancestry;
    }
}
