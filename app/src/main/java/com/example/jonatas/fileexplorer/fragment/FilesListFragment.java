package com.example.jonatas.fileexplorer.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jonatas.fileexplorer.MainActivity;
import com.example.jonatas.fileexplorer.R;
import com.example.jonatas.fileexplorer.adapter.FileAdapter;
import com.example.jonatas.fileexplorer.asynctask.LoadFilesTask;
import com.example.jonatas.fileexplorer.helper.FileItemsHelper;
import com.example.jonatas.fileexplorer.interfaces.CallBackLoadFiles;
import com.example.jonatas.fileexplorer.interfaces.ClickListener;
import com.example.jonatas.fileexplorer.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FilesListFragment extends Fragment implements ClickListener {
    private static final String DIRECTORY = "directory";
    private String directory;
    private RecyclerView recyclerView;
    private FileAdapter mFileAdapter;
    private List<FileItem> files;
    private GridLayoutManager gridLayoutManager;
    private ActionMode mActionMode;
    private FileItemsHelper fileHelper;
    private ActionModeCallback mActionModeCallback = new ActionModeCallback();



    public static FilesListFragment newInstance(String dir) {
        FilesListFragment fragment = new FilesListFragment();
        Bundle args = new Bundle();
        args.putString(DIRECTORY, dir);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileHelper = new FileItemsHelper();
        directory = getArguments().getString(DIRECTORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentListView = inflater.inflate(R.layout.fragment_list_files, container, false);
        return fragmentListView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //verification created to handle with back button pressed
        verifyOrientation();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_files);
        recyclerView.setLayoutManager(gridLayoutManager);

        files = new ArrayList<FileItem>();
        final LoadFilesTask loadFilesTask = new LoadFilesTask(directory, new CallBackLoadFiles<FileItem>() {

            @Override
            public void onLoadSuccess(List<FileItem> fileItems) {
                files = fileItems;
                loadFilesInRecyclerView();
            }

            @Override
            public void onLoadFailure(Exception e) {
                e.printStackTrace();
            }
        });

        loadFilesTask.execute();

    }

    private void verifyOrientation() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridLayoutManager = new GridLayoutManager(getContext(), 5);
        else
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setPresentDirectory(directory);
    }

    public void loadFilesInRecyclerView() {
        mFileAdapter = new FileAdapter(files, FilesListFragment.this);
        recyclerView.setAdapter(mFileAdapter);

        mFileAdapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClicked(int position) {
        if (mActionMode != null) {
            toggleSelection(position);
        } else {
            if (files.get(position).isFolder()) {
                String newDir = directory +"/" + files.get(position).getNome() + "/";
                FilesListFragment listFragment = FilesListFragment.newInstance(newDir);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame_layout, listFragment).addToBackStack(directory).commit();
            } else {
                Intent intent = fileHelper.tryToOpenFile(new File(directory + files.get(position).getNome()));
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onItemLongClicked(int position) {
        if (mActionMode == null) {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        mFileAdapter.toggleSelection(position);

        int count = mFileAdapter.getSelectedItemCount();

        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(String.valueOf(count));
            mActionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_list_delete, menu);
            mFileAdapter.setActionModeEnabled(true);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_delete:

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                    dialog.setTitle("Delete");
                    dialog.setMessage("Are you sure you want delete?");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItemsSelecteds();
                            dialog.dismiss();
                            mode.finish();
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                default:
                    return false;
            }
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mFileAdapter.clearSelection();
            mActionMode = null;
            mFileAdapter.setActionModeEnabled(false);
        }
    }

    private void deleteItemsSelecteds() {
        List<Integer> selectedFiles = mFileAdapter.getSelectedItems();
        boolean couldDelete = true;
        for (Integer file : selectedFiles) {
            String nameFile = files.get(file).getNome();
            File fileToDelete = new File(directory + "/" + nameFile);
            if (fileToDelete.delete() == false)
                couldDelete = false;
        }

        if (couldDelete == false) {
            Toast.makeText(getContext(), "Some File cannot be deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).loadFragmentList(directory);
        }
        mFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getContext(), 5);
        }  else {
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
