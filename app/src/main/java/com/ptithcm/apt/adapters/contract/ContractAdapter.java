package com.ptithcm.apt.adapters.contract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.contract.ContractResponse;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder> {

    private List<ContractResponse> contractList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onContractClick(ContractResponse contract);
    }

    public ContractAdapter(List<ContractResponse> contractList, OnItemClickListener listener) {
        this.contractList = contractList;
        this.listener = listener;
    }

    public void setData(List<ContractResponse> newData) {
        if (newData == null) return;
        this.contractList.clear();
        this.contractList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        ContractResponse contract = contractList.get(position);

        holder.tvResidentName.setText(contract.getResidentName());
        holder.tvRoom.setText("Phòng: " + contract.getRoomNumber());

        // Chuyển đổi tên Role
        String roleStr = "OWNER".equals(contract.getRole()) ? "CHỦ HỘ" : "NGƯỜI THUÊ";
        holder.tvRole.setText(roleStr);

        String startDate = contract.getContractStart() != null ? contract.getContractStart() : "...";
        String endDate = contract.getContractEnd() != null ? contract.getContractEnd() : "...";
        holder.tvDates.setText(startDate + " đến " + endDate);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onContractClick(contract);
        });
    }

    @Override
    public int getItemCount() {
        return contractList != null ? contractList.size() : 0;
    }

    public static class ContractViewHolder extends RecyclerView.ViewHolder {
        TextView tvResidentName, tvRole, tvRoom, tvDates;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);
            // Đảm bảo các ID này khớp với file item_contract.xml của bạn
            tvResidentName = itemView.findViewById(R.id.tv_contract_resident_name);
            tvRole = itemView.findViewById(R.id.tv_contract_role);
            tvRoom = itemView.findViewById(R.id.tv_contract_room);
            tvDates = itemView.findViewById(R.id.tv_contract_dates);
        }
    }
}