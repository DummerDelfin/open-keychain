/*
 * Copyright (C) 2014 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sufficientlysecure.keychain.ui.linked;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.sufficientlysecure.keychain.R;
import org.sufficientlysecure.keychain.operations.results.OperationResult.OperationLog;
import org.sufficientlysecure.keychain.pgp.linked.LinkedCookieResource;
import org.sufficientlysecure.keychain.pgp.linked.resources.TwitterResource;

public class LinkedIdCreateTwitterStep2Fragment extends LinkedIdCreateFinalFragment {

    public static final String ARG_HANDLE = "handle";

    String mResourceHandle;
    String mResourceString;

    public static LinkedIdCreateTwitterStep2Fragment newInstance
            (String handle) {

        LinkedIdCreateTwitterStep2Fragment frag = new LinkedIdCreateTwitterStep2Fragment();

        Bundle args = new Bundle();
        args.putString(ARG_HANDLE, handle);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResourceString =
                TwitterResource.generate(getActivity(), mLinkedIdWizard.mFingerprint);

        mResourceHandle = getArguments().getString(ARG_HANDLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        view.findViewById(R.id.button_send).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                proofSend();
            }
        });

        view.findViewById(R.id.button_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                proofShare();
            }
        });

        ((TextView) view.findViewById(R.id.linked_tweet_published)).setText(
                Html.fromHtml(getString(R.string.linked_create_twitter_2_3, mResourceHandle))
        );

        return view;
    }

    @Override
    LinkedCookieResource getResource(OperationLog log) {
        return TwitterResource.searchInTwitterStream(mResourceHandle, mResourceString, log);
    }

    @Override
    protected View newView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.linked_create_twitter_fragment_step2, container, false);
    }

    private void proofShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mResourceString);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void proofSend() {

        Uri.Builder builder = Uri.parse("https://twitter.com/intent/tweet").buildUpon();
        builder.appendQueryParameter("text", mResourceString);
        Uri uri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);
    }

}
