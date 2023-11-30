/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ifpe.edu.br.servsimples.R
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper
import ifpe.edu.br.servsimples.managers.ServerManager
import ifpe.edu.br.servsimples.model.Service
import ifpe.edu.br.servsimples.model.User
import ifpe.edu.br.servsimples.util.PersistHelper
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger
import ifpe.edu.br.servsimples.util.ServerResponseCodeParser


class ServiceDetailFragment : Fragment() {

    private lateinit var mCurrentService: Service
    private val TAG: String = Companion::class.java.name

    private val RETRIEVE_USER_INFO_OK = 0
    private val RETRIEVE_USER_INFO_NOT_OK = 1

    private var mProfessionalUser: User? = null

    // Service fields
    private var mTvServiceName: TextView? = null
    private var mTvServiceDescription: TextView? = null
    private var mTvServiceCategory: TextView? = null
    private var mTvServiceValue: TextView? = null
    private var mTvServiceTime: TextView? = null

    // User fields
    private var mTvProfessionalName: TextView? = null
    private var mTvProfessionalBio: TextView? = null


    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RETRIEVE_USER_INFO_OK -> fillUserInfo()
                RETRIEVE_USER_INFO_NOT_OK -> showErrorMessage()
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(context, "Erro ao recuperar informações", Toast.LENGTH_LONG).show()
    }

    private fun fillUserInfo() {
        mTvProfessionalName?.text = mProfessionalUser?.name
        mTvProfessionalBio?.text = mProfessionalUser?.bio
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val changeTitle = kotlin.runCatching {
            activity?.title = "Detalhes do serviço"
        }
        changeTitle.onFailure {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "Can't retrieve activity")
        }
        val view = inflater.inflate(R.layout.fragment_service_detail, container, false)
        findViews(view)
        setListeners()
        fillServiceInfo()
        retrieveProfessionalInfo()
        return view
    }

    private fun retrieveProfessionalInfo() {
        val callback = object : IServerManagerInterfaceWrapper.ServerRequestCallback {
            override fun onSuccess(user: User?) {
                mProfessionalUser = user
                mHandler.sendEmptyMessage(RETRIEVE_USER_INFO_OK)
            }

            override fun onFailure(message: String?) {
                Toast.makeText(
                    context,
                    ServerResponseCodeParser.parseToString(message),
                    Toast.LENGTH_SHORT
                ).show()
                mHandler.sendEmptyMessage(RETRIEVE_USER_INFO_NOT_OK)
            }
        }

        val currentUser = PersistHelper.getCurrentUser(context)
        currentUser.services.add(mCurrentService)

        Thread {
            ServerManager.getInstance()
                .getProfessionalUserFromService(currentUser, callback)
        }.start()
    }

    private fun fillServiceInfo() {
        mTvServiceName?.text = mCurrentService.name
        mTvServiceDescription?.text = mCurrentService.description
        mTvServiceCategory?.text = mCurrentService.category
        mTvServiceValue?.text = mCurrentService.cost.value
        mTvServiceTime?.text = mCurrentService.cost.time
    }

    private fun setListeners() {

    }

    private fun findViews(view: View?) {
        // Service
        mTvServiceName = view?.findViewById(R.id.servicedetail_infoservice_tv_name)
        mTvServiceDescription = view?.findViewById(R.id.servicedetail_infoservice_tv_description)
        mTvServiceCategory = view?.findViewById(R.id.servicedetail_infoservice_tv_category)
        mTvServiceValue = view?.findViewById(R.id.servicedetail_infoservice_tv_value)
        mTvServiceTime = view?.findViewById(R.id.servicedetail_infoservice_tv_time)

        // Professional User
        mTvProfessionalName = view?.findViewById(R.id.servicedetail_professionalinfo_tv_name)
        mTvProfessionalBio = view?.findViewById(R.id.servicedetail_professionalinfo_tv_bio)
    }

    companion object {
        @JvmStatic
        fun newInstance(service: Service) =
            ServiceDetailFragment().apply {
                mCurrentService = service
            }
    }
}