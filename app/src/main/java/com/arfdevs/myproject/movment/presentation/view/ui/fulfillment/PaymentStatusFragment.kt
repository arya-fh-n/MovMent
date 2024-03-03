package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentPaymentStatusBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.NOTIFICATION_CHANNEL_ID
import com.arfdevs.myproject.movment.presentation.helper.Constants.NOTIF_UNIQUE_WORK
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSACTION_ID
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSACTION_MOVIE_COUNT
import com.arfdevs.myproject.movment.presentation.helper.NotificationWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentStatusFragment :
    BaseFragment<FragmentPaymentStatusBinding>(FragmentPaymentStatusBinding::inflate) {

    private val safeArgs: PaymentStatusFragmentArgs by navArgs()

    override fun initView() = with(binding) {
        tvPaymentStatusTitle.text = getString(R.string.tv_payment_status_title)
        tvPaymentStatusDetailTitle.text = getString(R.string.tv_payment_status_detail_title)
        tvPaymentStatusIdTitle.text = getString(R.string.tv_payment_status_transaction_id_title)

        tvPriceTitle.text = getString(R.string.tv_price_status_title)
        tvPaymentDateTitle.text = getString(R.string.tv_payment_date_title)

        btnGoBack.text = getString(R.string.btn_go_back_to_home)

        safeArgs.movieTransactionModel.let { transaction ->
            tvPaymentStatusId.text = transaction.transactionId
            tvPrice.text = getString(R.string.tv_payment_total, transaction.total)
            tvPaymentDate.text = transaction.date
            sendNotification(transaction)
        }
    }

    override fun initListener() = with(binding) {
        btnGoBack.setOnClickListener {
            activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
                ?.findNavController()
                ?.popBackStack()
        }
    }

    override fun initObserver() {

    }

    private fun sendNotification(transaction: MovieTransactionModel) {
        val workManager = WorkManager.getInstance(requireContext())
        val channelName = getString(R.string.notify_channel_name)
        val channelData = Data.Builder()
            .putString(NOTIFICATION_CHANNEL_ID, channelName)
            .putInt(TRANSACTION_ID, transaction.total)
            .putString(TRANSACTION_MOVIE_COUNT,
                view?.context?.getString(R.string.tv_item_transaction_total, transaction.movies.count())
            )
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(channelData)
            .build()

        CoroutineScope(Dispatchers.Default).launch {
            workManager.enqueueUniqueWork(NOTIF_UNIQUE_WORK,
                ExistingWorkPolicy.APPEND_OR_REPLACE, oneTimeWorkRequest)
            delay(500)
            workManager.cancelUniqueWork(NOTIF_UNIQUE_WORK)
        }
    }

}