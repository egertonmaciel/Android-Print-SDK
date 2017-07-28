/*****************************************************
 *
 * ARetainedFragment.java
 *
 *
 * Modified MIT License
 *
 * Copyright (c) 2010-2016 Kite Tech Ltd. https://www.kite.ly
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The software MAY ONLY be used with the Kite Tech Ltd platform and MAY NOT be modified
 * to be used with any competitor platforms. This means the software MAY NOT be modified
 * to place orders with any competitors to Kite Tech Ltd, all orders MUST go through the
 * Kite Tech Ltd platform servers.
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *****************************************************/

///// Package Declaration /////

package ly.kite.app;

///// Import(s) /////

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

///// Class Declaration /////

/*****************************************************
 *
 * This class is the parent class for retained fragments.
 * It provides helper methods for attaching to activities,
 * and providing callbacks.
 *
 *****************************************************/
public abstract class ARetainedFragment extends Fragment {
    ////////// Static Constant(s) //////////

    private static final String LOG_TAG = "ARetainedFragment";

    ////////// Static Variable(s) //////////

    ////////// Member Variable(s) //////////

    protected RetainedFragmentHelper mRetainedFragmentHelper;

    ////////// Static Initialiser(s) //////////

    ////////// Constructor(s) //////////

    public ARetainedFragment(Class<?> callbackClass) {

        mRetainedFragmentHelper = new RetainedFragmentHelper(this, callbackClass);
    }

    ////////// Static Method(s) //////////

    /*****************************************************
     *
     * Tries to find this fragment, and returns it.
     *
     *****************************************************/
    protected static Fragment findFragment(Activity activity, String tag, Class<? extends ARetainedFragment> fragmentClass) {

        final FragmentManager fragmentManager = activity.getFragmentManager();

        if (fragmentManager != null) {
            final Fragment foundFragment = fragmentManager.findFragmentByTag(tag);

            if (foundFragment != null) {
                final Class<?> foundFragmentClass = foundFragment.getClass();

                if (foundFragmentClass.equals(fragmentClass)) {
                    return foundFragment;
                }
            }
        }

        return null;
    }

    ////////// DialogFragment Method(s) //////////

    /*****************************************************
     *
     * Called when the fragment is created.
     *
     *****************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mRetainedFragmentHelper.onCreate(savedInstanceState);
    }

    /*****************************************************
     *
     * Called when the fragment is attached to an activity.
     *
     *****************************************************/
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        mRetainedFragmentHelper.onAttach(activity);
    }

    /*****************************************************
     *
     * Called when a target fragment is set.
     *
     *****************************************************/
    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {

        super.setTargetFragment(fragment, requestCode);

        mRetainedFragmentHelper.onSetTargetFragment(fragment, requestCode);
    }

    ////////// Method(s) //////////

    /*****************************************************
     *
     * Adds this fragment to the activity.
     *
     *****************************************************/
    public void addTo(Activity activity, String tag) {

        mRetainedFragmentHelper.addTo(activity, tag);
    }

    /*****************************************************
     *
     * Sets the current state notifier. The notifier may
     * get called twice, if there is both an attached activity
     * and a target fragment of the correct callback type.
     *
     *****************************************************/
    protected void setStateNotifier(RetainedFragmentHelper.AStateNotifier stateNotifier) {

        mRetainedFragmentHelper.setState(stateNotifier);
    }

    /*****************************************************
     *
     * Removes this fragment from the activity.
     *
     *****************************************************/
    public void removeFrom(Activity activity) {

        mRetainedFragmentHelper.removeFrom(activity);
    }

    /*****************************************************
     *
     * Removes this fragment from the activity and clears
     * its target fragment.
     *
     *****************************************************/
    public void removeFrom(Fragment fragment) {

        super.setTargetFragment(null, 0);

        final Activity activity = fragment.getActivity();

        if (activity != null) {
            removeFrom(activity);
        }
    }

    ////////// Inner Class(es) //////////

}

