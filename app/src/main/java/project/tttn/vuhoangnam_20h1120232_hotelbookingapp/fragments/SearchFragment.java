package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.CurrentGroupDetail;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.ProvinceSearchActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button btn_provinceSearch = view.findViewById(R.id.btn_provinceSearch);
        Button btnPeople = view.findViewById(R.id.btn_people);
        Button btnSearch = view.findViewById(R.id.btn_search);

        String provinceId;
        String provinceName;
        if (getArguments() != null) {
            provinceName = getArguments().getString("selectedProvinceName");
            provinceId = getArguments().getString("selectedProvinceId");
            btn_provinceSearch.setText(provinceName);
        } else {
            provinceId = null;
            provinceName = null;
        btn_provinceSearch.setText("Nhập điểm đến của bạn");
        }

        CurrentGroupDetail currentGroupDetail = CurrentGroupDetail.getInstance();

        String groupDetailFormat = String.valueOf(currentGroupDetail.getNumRoom()) + " phòng - "
                + String.valueOf(currentGroupDetail.getNumAdult()) + " người lớn - "
                + String.valueOf(currentGroupDetail.getNumKid()) + " trẻ em";
        btnPeople.setText(groupDetailFormat);
        updateCurrentGroupDetail(btnPeople.getText().toString());

        btnPeople.setOnClickListener(v -> {
            String currentText = btnPeople.getText().toString();
            int[] counts = parseCounts(currentText);
            GroupDetailBottomSheet bottomSheet = GroupDetailBottomSheet.newInstance(counts[0], counts[1], counts[2]);
            bottomSheet.setBottomSheetListener(result -> {
                btnPeople.setText(result);
                updateCurrentGroupDetail(result);
            });
            bottomSheet.show(getChildFragmentManager(), "PeopleBottomSheet");
        });

        btn_provinceSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.ProvinceSearchActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            if ("Nhập điểm đến của bạn".equals(btn_provinceSearch.getText().toString())) {
                Toast.makeText(getActivity(), "Xin hãy nhập điểm đến của bạn đã nhé!", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getActivity(), project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HotelListActivity.class);
                // Sử dụng provinceId đã được khai báo ở ngoài if
                intent.putExtra("provinceId", provinceId);
                intent.putExtra("provinceName", provinceName);
                startActivity(intent);
            }
        });

        return view;
    }

    private int[] parseCounts(String text) {
        try {
            // Example text: "3 phòng - 1 người lớn - 0 trẻ em"
            int[] counts = new int[3]; // [roomCount, adultCount, kidCount]
            String[] parts = text.split(" - ");
            counts[0] = Integer.parseInt(parts[0].split(" ")[0]); // Room count
            counts[1] = Integer.parseInt(parts[1].split(" ")[0]); // Adult count
            counts[2] = Integer.parseInt(parts[2].split(" ")[0]); // Kid count
            return counts;
        } catch (Exception e) {
            // Handle parsing error or return default values
            return new int[]{1, 1, 0}; // default values
        }
    }

    private void updateCurrentGroupDetail(String text) {
        CurrentGroupDetail currentGroupDetail = CurrentGroupDetail.getInstance();
        Pattern pattern = Pattern.compile("(\\d+) phòng - (\\d+) người lớn - (\\d+) trẻ em");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            int numRoom = Integer.parseInt(matcher.group(1));
            int numAdult = Integer.parseInt(matcher.group(2));
            int numKid = Integer.parseInt(matcher.group(3));

            currentGroupDetail.setNumRoom(numRoom);
            currentGroupDetail.setNumAdult(numAdult);
            currentGroupDetail.setNumKid(numKid);
        } else {
            Log.e("GetGroupDetail", "Something wrong");
        }
    }



}