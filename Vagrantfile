# -*- mode: ruby -*-
# vi: set ft=ruby :

required_plugins = %w( vagrant-hostsupdater )
required_plugins.each do |plugin|
  system "vagrant plugin install #{plugin}" unless Vagrant.has_plugin? plugin
end

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "ubuntu/trusty32"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # portal
  # config.vm.network "forwarded_port", guest: 80, host: 80
  # workflow
  # config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 8443, host: 8443, auto_correct: true
  config.vm.network "forwarded_port", guest: 9000, host: 9000, auto_correct: true

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network "private_network", ip: "192.168.10.10"
  if not Vagrant::Util::Platform.windows?
    config.hostsupdater.aliases = ["e-gov-ua.dev", "admin.e-gov-ua.dev"]
  end

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder ".", "/project"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  # config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
  #   # Customize the amount of memory on the VM:
  #   vb.memory = "1024"
  # end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

   config.vm.provider "virtualbox" do |v, override|

    host = RbConfig::CONFIG['host_os']

    # Give VM 1/4 system memory & access to all cpu cores on the host
    if host =~ /darwin/
      v.cpus = `sysctl -n hw.ncpu`.to_i
      # sysctl returns Bytes and we need to convert to MB
      v.memory = `sysctl -n hw.memsize`.to_i / 1024 / 1024 / 4
    elsif host =~ /linux/
      v.cpus = `nproc`.to_i
      # meminfo shows KB and we need to convert to MB
      v.memory = `grep 'MemTotal' /proc/meminfo | sed -e 's/MemTotal://' -e 's/ kB//'`.to_i / 1024 / 4
    else
      # TODO get info for Windows
      # v.cpus = 2
      # v.memory = 1024
    end

    # Improve network download/upload speed
    v.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    v.customize ["modifyvm", :id, "--natdnsproxy1", "on"]

    # Config shared folder settings
    if Vagrant::Util::Platform.windows?
      override.vm.synced_folder ".", "/project", disabled: true
      #v.customize ["sharedfolder", "add", :id, "--name", "projectshare", "--hostpath", (("//?/" + File.dirname(__FILE__)).gsub("/","\\"))]
      v.customize ["setextradata", :id, "VBoxInternal2/SharedFoldersEnableSymlinksCreate/projectshare", "1"]
    else
      # Use NFS for shared folders for better performance
      override.vm.synced_folder ".", "/project", nfs: true
    end
  end
  # Define a Vagrant Push strategy for pushing to Atlas. Other push strategies
  # such as FTP and Heroku are also available. See the documentation at
  # https://docs.vagrantup.com/v2/push/atlas.html for more information.
  # config.push.define "atlas" do |push|
  #   push.app = "YOUR_ATLAS_USERNAME/YOUR_APPLICATION_NAME"
  # end

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  # config.vm.provision "shell", inline: <<-SHELL
  #   sudo apt-get update
  #   sudo apt-get install -y apache2
  # SHELL
  if Vagrant::Util::Platform.windows?
    config.vm.provision :shell, inline: "mkdir -p /project"
    config.vm.provision :shell, inline: "mount -t vboxsf -o uid=`id -u vagrant`,gid=`getent group vagrant | cut -d: -f3` projectshare /project", run: "always"
  end
  config.vm.provision :shell, path: "scripts/prepare_machine.sh"
  config.vm.provision :shell, privileged: false, path: "scripts/clean_wf.sh"
  config.vm.provision :shell, privileged: false, run: "always", path: "scripts/update_wf_fast.sh"
  config.vm.provision :shell, privileged: false, run: "always", path: "scripts/up_central_js.sh"
  config.vm.provision :shell, privileged: false, run: "always", path: "scripts/up_dashboard_js.sh"

    config.vm.post_up_message = 
"To open grunt output, connect to vagrant 'vagrant ssh' and type 'screen -r central-js'
 or 'screen -r dashboard-js' (detach screen 'ctrl+a+d')
******  Application stated    *********************
*******  You can use VHOSTS    ********************
http(s)://e-gov-ua.dev  =>   https://192.168.10.10:8443/
http(s)://admin.e-gov-ua.dev  =>   http://192.168.10.10:9000/
http://e-gov-ua.dev/wf-region/ =>   http://192.168.10.10:8080/wf-region/"

end
